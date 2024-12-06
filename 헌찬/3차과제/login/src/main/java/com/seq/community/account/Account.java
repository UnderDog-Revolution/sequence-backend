package com.seq.community.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public Date birthDate;

    @Column(nullable = false)
    public String gender;

    @Column(nullable = false)
    public String phoneNumber;

    @Column(nullable = false)
    public String email;

    @Column(nullable = false)
    public String address;

    // 마이 프로필 정보
    @Column
    public String profilePictureUrl;

    @Column
    public String nickname;

    @Column
    public String education;

    @Column
    public String skills;

    @Column
    public String desiredRole;

    @Column(length = 2000)
    public String experience;

    @Column(length = 2000)
    public String career;

    @Column(length = 2000)
    public String certificationsAndAwards;

    @Column(length = 2000)
    public String portfolio;

    @Column(length = 2000)
    public String selfIntroduction;
}
