package com.example.login_logout.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String status;
    private String token;
    private String refreshToken;

    public LoginResponse(String status, String token, String refreshToken) {
        this.status = status;
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
