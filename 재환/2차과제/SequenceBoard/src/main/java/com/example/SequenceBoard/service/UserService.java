package com.example.SequenceBoard.service;

import com.example.SequenceBoard.dto.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    
    public String getCurrentUsername(String token) {
        ResponseEntity<UserDto> response = restTemplate.exchange(
            "http://localhost:8080/api/user/me",
            HttpMethod.GET,
            new HttpEntity<>(createHeaders(token)),
            UserDto.class
        );
        return response.getBody().getUsername();
    }
    
    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return headers;
    }
}