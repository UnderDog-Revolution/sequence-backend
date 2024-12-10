package com.example.login_logout.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String password;
    private String username;

    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String profileImage; // 프로필 사진 URL 또는 경로
    private String nickname; // 별명
    private String education; // 학력
    private String skills; // 스킬 목록 (예: Java, Spring, React 등)
    private String desiredRole; // 희망 역할 (예: 백엔드 개발자)
    private String experience; // 경험 및 활동 이력
    private String career; // 경력
    private String certificationsAndAwards; // 자격 및 수상
    private String portfolioLink; // 포트폴리오 링크
    private String introduction; // 자기소개
}
