package sequence.sequence_member.deleteAccount.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sequence.sequence_member.deleteAccount.dto.ApiResponseData;
import sequence.sequence_member.deleteAccount.dto.ApiResponseError;
import sequence.sequence_member.deleteAccount.dto.Code;
import sequence.sequence_member.deleteAccount.repository.MemberRepository;
import sequence.sequence_member.deleteAccount.util.JwtDecoder;
import sequence.sequence_member.deleteAccount.service.DeletedUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import sequence.sequence_member.deleteAccount.dto.AccountDto;

import java.util.Map;


@RestController
public class DeleteController {

    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final DeletedUserService deletedUserService;
    private final JwtDecoder jwtDecoder = new JwtDecoder();
    private final MemberRepository memberRepository;

    @Autowired
    public DeleteController(RestTemplate restTemplate, PasswordEncoder passwordEncoder, DeletedUserService deletedUserService, MemberRepository memberRepository) {
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
        this.deletedUserService = deletedUserService;
        this.memberRepository = memberRepository;
    }

    // 사용자 탈퇴 API
    @DeleteMapping("/api/user/delete")
    @ResponseBody
    public ResponseEntity<?> deleteProcess(@RequestBody AccountDto user, HttpServletRequest request) {

        String tokenResult;
        String token = jwtDecoder.getTokenFromCookies(request, "access");

        if (token == null) {
            return ResponseEntity.status(Code.NULL_INPUT_VALUE.getStatus())
                    .body(ApiResponseError.of(Code.NULL_INPUT_VALUE, "쿠키에서 토큰을 가져오지 못했습니다."));
        }

        try {
            tokenResult = jwtDecoder.decodePayload(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(Code.INVALID_TOKEN.getStatus())
                    .body(ApiResponseError.of(Code.INVALID_TOKEN, "Invalid token: " + e.getMessage()));
        }

        // JSON 파싱 및 name 추출
        String username;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(tokenResult);
            username = jsonNode.get("username").asText();
        } catch (Exception e) {
            return ResponseEntity.status(Code.INVALID_TOKEN.getStatus())
                    .body(ApiResponseError.of(Code.INVALID_TOKEN, "토큰 페이로드를 불러오지 못했습니다."));
        }

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
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(Code.NULL_INPUT_VALUE.getStatus()).body(
                    ApiResponseError.of(Code.NULL_INPUT_VALUE)
            );
        }
        if (user.getConfirm_password() == null || user.getConfirm_password().isEmpty()) {
            return ResponseEntity.status(Code.NULL_INPUT_VALUE.getStatus()).body(
                    ApiResponseError.of(Code.NULL_INPUT_VALUE)
            );
        }
        if (!user.getPassword().equals(user.getConfirm_password())) {
            return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(
                    ApiResponseError.of(Code.VALIDATION_ERROR, "비밀번호가 서로 다릅니다.")
            );
        }

//        //토큰의 유저 이름을 조회 비교
//        if (!passwordEncoder.matches(user.getPassword(), externalUser.getPassword())){
//            return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(
//                    ApiResponseError.of(Code.VALIDATION_ERROR, "비밀번호가 일치하지 않습니다.")
//            );
//        }

        //토큰의 유저 이름을 조회 비교 (테스트용)
        if (!user.getPassword().equals(externalUser.getPassword())){
            return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(
                    ApiResponseError.of(Code.VALIDATION_ERROR, "비밀번호가 일치하지 않습니다.")
            );
        }

        // 삭제된 사용자 기록 저장
        deletedUserService.saveDeletedUser(
                externalUser.getId(),
                externalUser.getUsername(),
                true,
                "사용자 탈퇴 요청"
        );

        //성공 응답 반환
        return ResponseEntity.status(Code.SUCCESS.getStatus()).body(
                ApiResponseData.of(externalUser, Code.SUCCESS.getMessage())
        );
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
