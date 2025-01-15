package com.seq.deleteAccount.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seq.deleteAccount.dto.ApiResponseData;
import com.seq.deleteAccount.dto.ApiResponseError;
import com.seq.deleteAccount.dto.Code;
import com.seq.deleteAccount.entity.DeletedUserEntity;
import com.seq.deleteAccount.repository.DeletedUserRepository;
import com.seq.deleteAccount.repository.MemberRepository;
import com.seq.deleteAccount.util.JwtDecoder;
import com.seq.deleteAccount.service.DeletedUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import com.seq.deleteAccount.dto.AccountDto;

import java.util.Arrays;


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


    @DeleteMapping("/api/user/delete")
    @ResponseBody
    public ResponseEntity<?> deleteProcess(@RequestBody AccountDto user, HttpServletRequest request) {

        String tokenResult;
        String token = jwtDecoder.getTokenFromCookies(request, "JWT");

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


//        //외부 API를 통해 계정 정보 가져오기
////        String externalApiUrl = "http://localhost:8081/api/user" + user.getUsername();
//        String externalApiUrl = "http://localhost:8081/api/user/info";
//        AccountDto externalAccount;
//
//        try {
//            externalAccount = restTemplate.getForObject(externalApiUrl, AccountDto.class);
//        } catch (Exception e) {
//            return ResponseEntity.status(Code.CAN_NOT_FIND_RESOURCE.getStatus()).body(
//                    ApiResponseError.of(Code.CAN_NOT_FIND_RESOURCE)
//            );
//        }


//        //외부 계정 정보의 해시된 비밀번호와 입력된 비밀번호 비교
//        if (!passwordEncoder.matches(user.getPassword(), externalAccount.getPassword())) {
//            return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(
//                    ApiResponseError.of(Code.VALIDATION_ERROR, "비밀번호가 일치하지 않습니다.")
//            );
//        }

        //토큰의 유저 이름을 조회 비교
        if (!passwordEncoder.matches(user.getPassword(), externalUser.getPassword())){
            return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(
                    ApiResponseError.of(Code.VALIDATION_ERROR, "비밀번호가 일치하지 않습니다.")
            );
        }

//        //외부 계정 정보의 해시된 비밀번호와 입력된 비밀번호 비교
//        if (!externalUser.getPassword().equals(user.getPassword())) {
//            return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(
//                    ApiResponseError.of(Code.VALIDATION_ERROR, "비밀번호가 일치하지 않습니다.")
//            );
//        }

        // 삭제된 사용자 기록 저장
        deletedUserService.saveDeletedUser(
                externalUser.getUsername(),
                true,
                "사용자 탈퇴 요청"
        );

        //성공 응답 반환
        return ResponseEntity.status(Code.SUCCESS.getStatus()).body(
                ApiResponseData.of(externalUser, Code.SUCCESS.getMessage())
        );
    }

}
