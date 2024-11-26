package com.example.Sequence.controller;

import com.example.Sequence.dto.response.ErrorResponse;
import com.example.Sequence.dto.response.TokenRefreshResponse;
import com.example.Sequence.entity.User;
import com.example.Sequence.service.UserService;
import com.example.Sequence.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

    @GetMapping("/validate")
    public ResponseEntity<?> validateUser(
        @RequestHeader("Authorization") String token,
        @RequestParam String userId,
        @RequestParam String username
    ) {
        log.info("=== Received Token ===");
        log.info("Raw token: {}", token);
        log.info("userId: {}", userId);
        log.info("username: {}", username);
        
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            log.info("Processed token: {}", actualToken);
            
            try {
                Claims claims = jwtUtil.validateAndGetClaims(actualToken);
                String tokenUserId = claims.get("userId", String.class);
                log.info("=== Token Validation ===");
                log.info("Token userId: {}", tokenUserId);
                log.info("Request userId: {}", userId);
                
                if (!tokenUserId.equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ValidationResponse(false, "토큰의 사용자와 요청 사용자가 일치하지 않습니다."));
                }

                User user = userService.findById(userId);
                if (user == null) {
                    log.info("User not found with id: {}", userId);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ValidationResponse(false, "존재하지 않는 사용자입니다."));
                }

                log.info("=== Username Validation ===");
                log.info("DB username: '{}'", user.getName());
                log.info("Request username: '{}'", username);
                log.info("DB username length: {}", user.getName().length());
                log.info("Request username length: {}", username.length());
                log.info("Are they equal? {}", user.getName().equals(username));
                
                if (!user.getName().equals(username)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ValidationResponse(false, "사용자 이름 검증 실패"));
                }

                return ResponseEntity.ok(new ValidationResponse(true, "유효한 사용자입니다."));
                
            } catch (ExpiredJwtException e) {
                log.error("Token has expired");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ValidationResponse(false, "토큰이 만료되었습니다. 다시 로그인해주세요."));
            } catch (Exception e) {
                log.error("Token validation error: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ValidationResponse(false, "유효하지 않은 토큰입니다."));
            }

        } catch (Exception e) {
            log.error("Validation error: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ValidationResponse(false, "인증에 실패했습니다."));
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