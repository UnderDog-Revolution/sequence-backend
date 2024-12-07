package com.example.login_logout.member.controller;

import com.example.login_logout.member.dto.LoginRequest;
import com.example.login_logout.member.dto.LoginResponse;
import com.example.login_logout.member.dto.NewAccessTokenRequest;
import com.example.login_logout.member.dto.NewAccessTokenResponse;
import com.example.login_logout.member.dto.RegisterRequest;
import com.example.login_logout.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse response = memberService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    // 회원가입
    @PostMapping("/user")
    public ResponseEntity<String> signup(@RequestBody @Valid RegisterRequest registerRequest) {
        memberService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    // 아이디 중복 확인

    // JWT 재발급
    @PostMapping("/token")
    public ResponseEntity<NewAccessTokenResponse> refreshAccessToken(@RequestBody NewAccessTokenRequest request) {
        NewAccessTokenResponse response = memberService.newAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
