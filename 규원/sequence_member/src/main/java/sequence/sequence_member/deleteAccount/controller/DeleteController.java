package sequence.sequence_member.deleteAccount.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sequence.sequence_member.deleteAccount.dto.DeleteDto;
import sequence.sequence_member.member.jwt.JWTUtil;
import sequence.sequence_member.member.repository.MemberRepository;
import sequence.sequence_member.member.repository.RefreshRepository;
import sequence.sequence_member.member.response.ResponseMsg;
import sequence.sequence_member.deleteAccount.service.DeletedService;


@RestController
public class DeleteController {
    private final DeletedService deletedUserService;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshRepository refreshRepository;

    @Autowired
    public DeleteController(
            DeletedService deletedUserService,
            MemberRepository memberRepository,
            JWTUtil jwtUtil,
            BCryptPasswordEncoder bCryptPasswordEncoder, RefreshRepository refreshRepository)
    {
        this.deletedUserService = deletedUserService;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.refreshRepository = refreshRepository;
    }

    // 사용자 탈퇴 API
    @DeleteMapping("/api/user/delete")
    @ResponseBody
    public ResponseEntity<?> deleteProcess(@RequestBody DeleteDto deleteDto, HttpServletRequest request) {

        // 쿠키에서 토큰 추출
        String refresh = extractRefreshToken(request);
        if (refresh == null) {
            return ResponseEntity.badRequest().body(new ResponseMsg(40201, "토큰을 찾을 수 없습니다.", null));
        }

        // DB에 refresh 토큰이 존재하는지 확인
        if (!refreshRepository.existsByRefresh(refresh)) {
            return ResponseEntity.badRequest().body(new ResponseMsg(40202, "토큰이 만료되었습니다.", null));
        }

        // 토큰에서 유저 이름 추출 및 존재 여부 확인
        String username = jwtUtil.getUsername(refresh);
        var externalUser = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 비밀번호 체크
        ResponseEntity<?> passwordValidationResponse = validatePassword(deleteDto, externalUser.getPassword());
        if (passwordValidationResponse != null) return passwordValidationResponse;

        // 탈퇴 기록 저장
        deletedUserService.saveDeletedUser(
                externalUser.getId(),
                externalUser.getUsername(),
                true,
                "사용자 탈퇴 요청"
        );

        // user 삭제
        deletedUserService.deleteUser(externalUser.getId());

        // refresh 토큰 삭제
        refreshRepository.deleteByRefresh(refresh);

        // 쿠키에서 refresh 토큰 제거
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return ResponseEntity.ok(new ResponseMsg(0, "회원 탈퇴가 완료되었습니다.", null));
    }


    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    private ResponseEntity<?> validatePassword(DeleteDto deleteDto, String encodedPassword) {
        if (deleteDto.getPassword() == null || deleteDto.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMsg(40002, "비밀번호를 입력하세요.", null));
        }
        if (deleteDto.getConfirm_password() == null || deleteDto.getConfirm_password().isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMsg(40002, "비밀번호 확인을 입력하세요.", null));
        }
        if (!deleteDto.getPassword().equals(deleteDto.getConfirm_password())) {
            return ResponseEntity.badRequest().body(new ResponseMsg(40003, "비밀번호가 서로 다릅니다.", null));
        }
        if (!bCryptPasswordEncoder.matches(deleteDto.getPassword(), encodedPassword)) {
            return ResponseEntity.badRequest().body(new ResponseMsg(40004, "비밀번호가 일치하지 않습니다.", null));
        }
        return null;
    }

    // userId로 탈퇴 여부를 확인하는 API
    @GetMapping("/api/user/isDeleted/id")
    @ResponseBody
    public ResponseEntity<?> checkIfUserIsDeleted(@RequestParam(name = "userId",required = false) Long userId) {
        // 탈퇴 여부 확인
        boolean isDeleted = deletedUserService.isDeletedUser(userId);
        String result = "탈퇴되지 않은 사용자입니다.";

        if (isDeleted) {
            result = "탈퇴된 사용자입니다.";
        }

        ResponseMsg responseMsg = new ResponseMsg(0, result, null);
        return ResponseEntity.ok(responseMsg);
    }
    
    // username으로 탈퇴 여부를 확인하는 API
    @GetMapping("/api/user/isDeleted/username")
    @ResponseBody
    public ResponseEntity<?> checkIfUserIsDeletedWithUsername(@RequestParam(name = "username",required = false) String username) {
        // 탈퇴 여부 확인
        boolean isDeleted = deletedUserService.isDeletedUser(username);
        String result = "탈퇴되지 않은 사용자입니다.";

        if (isDeleted) {
            result = "탈퇴된 사용자입니다.";
        }

        ResponseMsg responseMsg = new ResponseMsg(0, result, null);
        return ResponseEntity.ok(responseMsg);
    }

}
