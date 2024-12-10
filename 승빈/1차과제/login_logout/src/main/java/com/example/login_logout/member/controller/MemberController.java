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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> signup(@ModelAttribute @Valid RegisterRequest registerRequest) throws IOException {
        memberService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("status", "success", "message", "회원가입이 완료되었습니다."));
    }

    // 아이디 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, String>> checkUsername(@RequestParam("username") String username) {
        boolean exists = memberService.isUsernameExists(username);

        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "error", "message", "이미 존재하는 아이디입니다."));
        }

        return ResponseEntity.ok(Map.of("status", "success", "message", "사용 가능한 아이디입니다."));
    }

    // JWT 재발급
    @PostMapping("/token")
    public ResponseEntity<NewAccessTokenResponse> refreshAccessToken(@RequestBody NewAccessTokenRequest request) {
        NewAccessTokenResponse response = memberService.newAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
