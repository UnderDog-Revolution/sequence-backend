package com.example.login_logout.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewAccessTokenRequest {
    private String refreshToken;
}