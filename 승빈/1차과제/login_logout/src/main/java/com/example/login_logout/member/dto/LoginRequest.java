package com.example.login_logout.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "loginId is required")
    private String loginId;

    @NotBlank(message = "Password is required")
    private String password;
}
