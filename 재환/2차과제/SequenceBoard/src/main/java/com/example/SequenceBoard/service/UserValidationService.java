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
    
    @Cacheable(value = "userValidation", key = "#userId")
    @CircuitBreaker(name = "validateUser", fallbackMethod = "validateUserFallback")
    public boolean validateUser(String token, String userId) {
        try {
            log.debug("Sending validation request - Token: {}, UserId: {}", token, userId);
            
            ResponseEntity<ValidationResponse> response = 
                authServiceClient.validateUser(token, userId);
            
            log.debug("Validation response - Status: {}, Body: {}", 
                response.getStatusCode(), 
                response.getBody());
                
            return response.getBody() != null && response.getBody().isValid();
            
        } catch (FeignException e) {
            log.error("User validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    private boolean validateUserFallback(String token, String userId, Exception e) {
        log.warn("Circuit breaker fallback for user {}: {}", userId, e.getMessage());
        return false;
    }
} 