package com.example.Sequence.controller;

import com.example.Sequence.dto.request.RegisterRequest;
import com.example.Sequence.dto.response.FileUploadResponse;
import com.example.Sequence.dto.response.RegisterResponse;
import com.example.Sequence.entity.User;
import com.example.Sequence.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.Sequence.dto.response.ErrorResponse;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = User.builder()
                    .id(request.getId())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .birthDate(request.getBirthDate())
                    .gender(request.getGender())
                    .address(request.getAddress())
                    .phoneNumber(request.getPhoneNumber())
                    .email(request.getEmail())
                    .schoolName(request.getSchoolName())
                    .majorName(request.getMajorName())
                    .entranceYear(request.getEntranceYear())
                    .graduationYear(request.getGraduationYear())
                    .academicStatus(request.getAcademicStatus())
                    .skills(request.getSkills() != null ? String.join(",", request.getSkills()) : "")
                    .desiredPositions(request.getDesiredPositions() != null ? String.join(",", request.getDesiredPositions()) : "")
                    .portfolioUrl(request.getPortfolioUrl())
                    .introduction(request.getIntroduction())
                    .build();

            userService.register(user);
            return ResponseEntity.ok(new RegisterResponse(user.getId(), "회원가입이 완료되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/register/portfolio")
    public ResponseEntity<?> uploadPortfolio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) {
        try {
            String fileName = userService.savePortfolioFile(file, userId);
            return ResponseEntity.ok(new FileUploadResponse(fileName));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/register/profile-image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) {
        try {
            String fileName = userService.saveProfileImage(file, userId);
            return ResponseEntity.ok(new FileUploadResponse(fileName));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
