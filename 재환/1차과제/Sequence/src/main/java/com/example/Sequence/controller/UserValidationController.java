package com.example.Sequence.controller;

import com.example.Sequence.dto.response.ErrorResponse;
import com.example.Sequence.dto.response.TokenRefreshResponse;
import com.example.Sequence.entity.User;
import com.example.Sequence.service.UserService;
import com.example.Sequence.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j  // 로깅을 위해 추가
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserValidationController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestHeader("Authorization") String token,
                                          @RequestParam String userId) {
        log.debug("Received validation request - Token: {}, UserId: {}", token, userId);
        try {
            // Bearer 토큰 처리
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            log.debug("Processing token: {}", actualToken);

            // 토큰 검증
            Claims claims = jwtUtil.validateAndGetClaims(actualToken);
            String tokenUserId = claims.get("userId", String.class);
            log.debug("Token userId: {}, Request userId: {}", tokenUserId, userId);

            // 토큰의 사용자 아이디와 전달받은 사용자 아이디가 일치하는지 확인
            if (!tokenUserId.equals(userId)) {
                log.warn("UserId mismatch - Token: {}, Request: {}", tokenUserId, userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("토큰의 사용자와 요청 사용자가 일치하지 않습니다."));
            }

            // 사용자 존재 여부 확인
            boolean exists = userService.existsByUserId(userId);
            log.debug("User exists check result: {}", exists);

            if (!exists) {
                log.warn("User not found: {}", userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("존재하지 않는 사용자입니다."));
            }

            log.debug("Validation successful for userId: {}", userId);
            return ResponseEntity.ok(new ValidationResponse(true, "유효한 사용자입니다."));

        } catch (Exception e) {
            log.error("Validation error: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("유효하지 않은 토큰입니다."));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        try {
            String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;
            
            // 리프레시 토큰 검증
            Claims claims = jwtUtil.validateAndGetClaims(token);
            String userId = claims.get("userId", String.class);
            
            // DB에 저장된 리프레시 토큰과 비교
            if (!userService.validateRefreshToken(userId, token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("유효하지 않은 리프레시 토큰입니다."));
            }
            
            // 새로운 액세스 토큰 발급
            User user = userService.findById(userId);
            String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getName());
            
            return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("토큰 갱신에 실패했습니다."));
        }
    }
}

@Getter
@AllArgsConstructor
class ValidationResponse {
    private boolean valid;
    private String message;
} 