package com.example.Sequence.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private String name;
    private String accessToken;
    private String refreshToken;
} 