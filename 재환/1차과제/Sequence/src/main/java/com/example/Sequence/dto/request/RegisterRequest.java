package com.example.Sequence.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    private String id;
    private String password;
    private String name;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;
    private String schoolName;
    private String majorName;
    private String entranceYear;
    private String graduationYear;
    private String academicStatus;
    private List<String> skills;
    private List<String> desiredPositions;
    private String portfolioUrl;
    private String introduction;
} 