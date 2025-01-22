package sequence.sequence_member.member.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sequence.sequence_member.deleteAccount.dto.ApiResponseData;
import sequence.sequence_member.deleteAccount.dto.ApiResponseError;
import sequence.sequence_member.deleteAccount.dto.Code;
import sequence.sequence_member.member.dto.DeleteDto;
import sequence.sequence_member.member.dto.MemberDTO;
import sequence.sequence_member.member.jwt.JWTUtil;
import sequence.sequence_member.member.repository.MemberRepository;
import sequence.sequence_member.member.response.ResponseMsg;
import sequence.sequence_member.member.service.DeletedService;


import java.util.Map;


@RestController
public class DeleteController {
    private final DeletedService deletedUserService;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DeleteController(
            DeletedService deletedUserService,
            MemberRepository memberRepository,
            JWTUtil jwtUtil,
            BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.deletedUserService = deletedUserService;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

        String username = jwtUtil.getUsername(refresh);

        var result = memberRepository.findByUsername(username);
        var externalUser = result.get();

        //중복 탈퇴 확인
        if (deletedUserService.isDeletedUser(externalUser.getUsername())) {
            return ResponseEntity.status(Code.DUPLICATE_RESOURCE.getStatus()).body(
                    ApiResponseError.of(Code.DUPLICATE_RESOURCE, "이미 탈퇴된 계정입니다.")
            );
        }

        //빈칸 확인
        //Password와 ConfirmPassword 비교
        if (deleteDto.getPassword() == null || deleteDto.getPassword().isEmpty()) {
            return ResponseEntity.status(Code.NULL_INPUT_VALUE.getStatus()).body(
                    ApiResponseError.of(Code.NULL_INPUT_VALUE)
            );
        }
        if (deleteDto.getConfirm_password() == null || deleteDto.getConfirm_password().isEmpty()) {
            return ResponseEntity.status(Code.NULL_INPUT_VALUE.getStatus()).body(
                    ApiResponseError.of(Code.NULL_INPUT_VALUE)
            );
        }
        if (!deleteDto.getPassword().equals(deleteDto.getConfirm_password())) {
            return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(
                    ApiResponseError.of(Code.VALIDATION_ERROR, "비밀번호가 서로 다릅니다.")
            );
        }

        //토큰의 유저 이름을 조회 비교
        if (!bCryptPasswordEncoder.matches(deleteDto.getPassword(), externalUser.getPassword())) {
            return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(
                    ApiResponseError.of(Code.VALIDATION_ERROR, "비밀번호가 일치하지 않습니다.")
            );
        }

//        //토큰의 유저 이름을 조회 비교 (테스트용)
//        if (!deleteDto.getPassword().equals(externalUser.getPassword())){
//            return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(
//                    ApiResponseError.of(Code.VALIDATION_ERROR, "비밀번호가 일치하지 않습니다.")
//            );
//        }

        // 삭제된 사용자 기록 저장
        deletedUserService.saveDeletedUser(
                externalUser.getId(),
                externalUser.getUsername(),
                true,
                "사용자 탈퇴 요청"
        );

        deletedUserService.deleteUser(externalUser.getId());

        //성공 응답 반환
        ResponseMsg responseMsg = new ResponseMsg(0, "회원 탈퇴가 완료되었습니다.", null);
        return ResponseEntity.ok(responseMsg);
    }

    // userId로 탈퇴 여부를 확인하는 API
    @GetMapping("/api/user/isDeleted/id/{id}")
    @ResponseBody
    public ResponseEntity<?> checkIfUserIsDeleted(@PathVariable("id") Long userId) {
        // 탈퇴 여부 확인
        boolean isDeleted = deletedUserService.isDeletedUser(userId);

        // 응답 생성
        return ResponseEntity.ok(ApiResponseData.of(
                Map.of("userId", userId, "isDeleted", isDeleted),
                isDeleted ? "탈퇴된 사용자입니다." : "탈퇴되지 않은 사용자입니다."
        ));
    }

    // username으로 탈퇴 여부를 확인하는 API
    @GetMapping("/api/user/isDeleted/username/{username}")
    @ResponseBody
    public ResponseEntity<?> checkIfUserIsDeletedWithUsername(@PathVariable("username") String username) {
        // 탈퇴 여부 확인
        boolean isDeleted = deletedUserService.isDeletedUser(username);

        // 응답 생성
        return ResponseEntity.ok(ApiResponseData.of(
                Map.of("username", username, "isDeleted", isDeleted),
                isDeleted ? "탈퇴된 사용자입니다." : "탈퇴되지 않은 사용자입니다."
        ));
    }

}
