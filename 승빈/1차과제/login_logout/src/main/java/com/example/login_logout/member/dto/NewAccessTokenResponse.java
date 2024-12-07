package com.example.login_logout.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewAccessTokenResponse {
    private String status;
    private String token;

    public NewAccessTokenResponse(String status, String token) {
        this.status = status;
        this.token = token;
    }
}
