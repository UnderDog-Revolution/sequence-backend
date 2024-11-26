package com.example.SequenceBoard.controller;

import com.example.SequenceBoard.dto.ArticleRequestDto;
import com.example.SequenceBoard.dto.ArticleResponseDto;
import com.example.SequenceBoard.service.ArticleService;
import com.example.SequenceBoard.service.UserValidationService;
import com.example.SequenceBoard.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleController {
    
    private final ArticleService articleService;
    private final UserValidationService userValidationService;
    
    @PostMapping("/articles")
    public ResponseEntity<ArticleResponseDto> createArticle(
        @RequestBody ArticleRequestDto requestDto,
        @RequestHeader("Authorization") String token
    ) {
        String authenticatedUserId = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal()
            .toString();
        
        log.debug("AuthenticatedUserId: {}", authenticatedUserId);
        
        if (userValidationService.validateUser(token, authenticatedUserId)) {
            ArticleResponseDto response = articleService.createArticle(requestDto);
            return ResponseEntity.ok(response);
        }
        
        throw new UnauthorizedException("인증되지 않은 사용자입니다.");
    }
} 