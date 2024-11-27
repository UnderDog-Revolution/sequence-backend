package com.example.SequenceBoard.client;

import com.example.SequenceBoard.dto.ValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "${auth.service.url}")
public interface AuthServiceClient {
    @GetMapping("/api/auth/validate")
    ResponseEntity<ValidationResponse> validateUser(
        @RequestHeader("Authorization") String token,
        @RequestParam("userId") String userId,
        @RequestParam("username") String username
    );
} 