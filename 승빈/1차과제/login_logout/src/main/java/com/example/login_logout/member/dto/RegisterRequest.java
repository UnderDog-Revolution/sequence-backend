package com.example.login_logout.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "name is required")
    private String name;
    @NotNull(message = "birthday is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @NotBlank(message = "gender is required")
    private String gender;
    @NotBlank(message = "phone is required")
    private String phone;
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "address is required")
    private String address;
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;

    @NotNull(message = "profileImage is required")
    private MultipartFile profileImage;
    @NotBlank(message = "nickname is required")
    private String nickname; // 별명
    @NotBlank(message = "education is required")
    private String education; // 학력
    @NotBlank(message = "skills is required")
    private String skills; // 스킬 목록
    @NotBlank(message = "desiredRole is required")
    private String desiredRole; // 희망 역할
    @NotBlank(message = "experience is required")
    private String experience; // 경험 및 활동 이력
    @NotBlank(message = "career is required")
    private String career; // 경력
    @NotBlank(message = "certificationsAndAwards is required")
    private String certificationsAndAwards; // 자격 및 수상
    @NotBlank(message = "portfolioLink is required")
    private String portfolioLink; // 포트폴리오 링크
    @NotBlank(message = "introduction is required")
    private String introduction; // 자기소개
}
