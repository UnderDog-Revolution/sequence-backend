package com.example.login_logout.validate.controller;

import com.example.login_logout.validate.service.MemberValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member/validate")
public class MemberValidationController {

    private final MemberValidationService validationService;

    public MemberValidationController(MemberValidationService validationService) {
        this.validationService = validationService;
    }

    @GetMapping("/{memberId}/exists")
    public ResponseEntity<Boolean> validateMember(@PathVariable("memberId") Long memberId) {
        boolean isValid = validationService.validateMember(memberId);

        if (isValid) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }
}
