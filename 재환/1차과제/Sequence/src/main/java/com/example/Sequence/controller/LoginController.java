package com.example.Sequence.controller;

import com.example.Sequence.dto.request.LoginRequest;
import com.example.Sequence.dto.response.ErrorResponse;
import com.example.Sequence.dto.response.LoginResponse;
import com.example.Sequence.entity.User;
import com.example.Sequence.service.UserService;
import com.example.Sequence.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest.getId(), loginRequest.getPassword());
            
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getName());
                String refreshToken = jwtUtil.generateRefreshToken(user.getId());
                
                userService.saveRefreshToken(user.getId(), refreshToken);
                
                return ResponseEntity.ok(new LoginResponse(
                        "로그인에 성공했습니다.",
                    user.getName(), 
                    accessToken, 
                    refreshToken
                ));
            } else {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("비밀번호가 일치하지 않습니다."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
        }
    }

}
