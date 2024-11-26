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
        String username = requestDto.getUsername();
        
        boolean isValid = userValidationService.validateUser(token, username);
        if (!isValid) {
            throw new UnauthorizedException("존재하지 않는 사용자입니다.");
        }
        
        ArticleResponseDto response = articleService.createArticle(requestDto, username);
        return ResponseEntity.ok(response);
    }
} 