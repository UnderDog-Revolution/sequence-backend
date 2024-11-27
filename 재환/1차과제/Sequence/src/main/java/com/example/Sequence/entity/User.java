package com.example.Sequence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false)
    private String name;            // 이름

    @Column(nullable = false)
    private LocalDate birthDate;    // 생년월일

    @Column(nullable = false)
    private String gender;          // 성별

    @Column(nullable = false)
    private String address;         // 주소지

    @Column(nullable = false, unique = true)
    private String phoneNumber;     // 휴대전화 번호

    @Column(nullable = false, unique = true)
    private String email;           // 이메일

    @Column(nullable = false)
    private String password;        // 비밀번호

    // 학력 관련
    private String schoolName;      // 학교명
    private String majorName;       // 전공명
    private String entranceYear;    // 입학연도
    private String graduationYear;  // 졸업연도
    private String academicStatus;  // 학적상태

    // 스킬
    @Column(length = 500)
    private String skills;

    // 희망직무
    @Column(length = 500)
    private String desiredPositions;

    // 포트폴리오 URL
    private String portfolioUrl;
    private String portfolioFile;

    // 자기소개
    @Column(length = 500)
    private String introduction;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Activity> activities = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Career> careers = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Certification> certifications = new ArrayList<>();

    private String profileImage;    // 프로필 이미지 파일명 저장

    // 연관관계 편의 메서드
    public void addActivity(Activity activity) {
        activities.add(activity);
        activity.setUser(this);
    }

    public void addCareer(Career career) {
        careers.add(career);
        career.setUser(this);
    }

    public void addCertification(Certification certification) {
        certifications.add(certification);
        certification.setUser(this);
    }
}