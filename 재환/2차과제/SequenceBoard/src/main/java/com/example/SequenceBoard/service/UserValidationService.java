package com.example.SequenceBoard.service;

import com.example.SequenceBoard.client.AuthServiceClient;
import com.example.SequenceBoard.dto.ValidationResponse;
import com.example.SequenceBoard.exception.UnauthorizedException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {
    
    private final AuthServiceClient authServiceClient;
    
    public boolean validateUser(String token, String userId, String username) {
        try {
            log.info("Sending GET validation request with params - userId: {}, username: {}", userId, username);
            
            ResponseEntity<ValidationResponse> response = 
                authServiceClient.validateUser(token, userId, username);
            
            log.info("Received validation response - Status: {}", response.getStatusCode());
            
            if (response.getBody() != null) {
                log.info("Response valid: {}, message: {}", response.getBody().isValid(), response.getBody().getMessage());
            }
            
            return response.getBody() != null && response.getBody().isValid();
            
        } catch (FeignException e) {
            log.error("User validation failed: {}", e.getMessage());
            return false;
        }
    }
} 