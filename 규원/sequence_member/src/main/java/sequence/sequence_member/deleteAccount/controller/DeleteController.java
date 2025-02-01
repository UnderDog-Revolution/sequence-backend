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

        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            ResponseMsg responseMsg = new ResponseMsg(40201, "토큰을 찾을 수 없습니다.", null);
            return ResponseEntity.badRequest().body(responseMsg);
        }

        //DB에 refresh 토큰이 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist){
            ResponseMsg responseMsg = new ResponseMsg(40202, "토큰이 만료되었습니다.", null);
            return ResponseEntity.badRequest().body(responseMsg);
        }

        String username = jwtUtil.getUsername(refresh);

        var result = memberRepository.findByUsername(username);
        var externalUser = result.get();

//        //중복 탈퇴 확인
//        if (deletedUserService.isDeletedUser(externalUser.getUsername())) {
//            ResponseMsg responseMsg = new ResponseMsg(40900, "이미 탈퇴된 계정입니다.", null);
//            return ResponseEntity.badRequest().body(responseMsg);
//        }

        //빈칸 확인
        //Password와 ConfirmPassword 비교
        if (deleteDto.getPassword() == null || deleteDto.getPassword().isEmpty()) {
            ResponseMsg responseMsg = new ResponseMsg(100, "비밀번호가 서로 다릅니다.", null);
            return ResponseEntity.badRequest().body(responseMsg);
        }

        if (deleteDto.getConfirm_password() == null || deleteDto.getConfirm_password().isEmpty()) {
            ResponseMsg responseMsg = new ResponseMsg(40002, "입력값이 없는 항목이 있습니다.", null);
            return ResponseEntity.badRequest().body(responseMsg);
        }
        if (!deleteDto.getPassword().equals(deleteDto.getConfirm_password())) {
            ResponseMsg responseMsg = new ResponseMsg(40002, "입력값이 없는 항목이 있습니다.", null);
            return ResponseEntity.badRequest().body(responseMsg);
        }

        //토큰의 유저 이름을 조회 비교
        if (!bCryptPasswordEncoder.matches(deleteDto.getPassword(), externalUser.getPassword())) {
            ResponseMsg responseMsg = new ResponseMsg(100, "비밀번호가 일치하지 않습니다.", null);
            return ResponseEntity.badRequest().body(responseMsg);
        }

        // 삭제된 사용자 기록 저장
        deletedUserService.saveDeletedUser(
                externalUser.getId(),
                externalUser.getUsername(),
                true,
                "사용자 탈퇴 요청"
        );

        //user 정보 삭제
        deletedUserService.deleteUser(externalUser.getId());

        //로그아웃 진행
        //refresh db에서 토큰 제거
        refreshRepository.deleteByRefresh(refresh);
        //Refresh 토큰 삭제 후, cookie값을 null로 처리해준다.
        //유효시간 값과 path 값, credential 값 등 여러가지를 처리해준다.
        Cookie cookie = new Cookie("refresh",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        //성공 응답 반환
        ResponseMsg responseMsg = new ResponseMsg(0, "회원 탈퇴가 완료되었습니다.", null);
        return ResponseEntity.ok(responseMsg);
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
