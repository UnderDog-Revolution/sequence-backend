package com.example.login_logout.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        String message = ex.getMessage();

        if (message.contains("이메일")) {
            response.put("error", message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404 상태
        }

        if (message.contains("비밀번호")) {
            response.put("error", message);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401 상태
        }

        response.put("error", "알 수 없는 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 상태
    }

    // InvalidInputException 처리
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, String>> handleInvalidInputException(InvalidInputException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 상태
    }

    // 다른 예외 처리 추가 가능
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 상태
    }
}
