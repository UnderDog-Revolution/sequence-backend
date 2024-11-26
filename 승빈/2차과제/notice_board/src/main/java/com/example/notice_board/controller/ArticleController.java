package com.example.notice_board.controller;

import com.example.notice_board.dto.ArticleRequestDto;
import com.example.notice_board.dto.ArticleResponseDto;
import com.example.notice_board.service.ArticleService;
import com.example.notice_board.validation.MemberValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;
    private final MemberValidationService memberValidationService;

    // 생성자 주입
    public ArticleController(ArticleService articleService, MemberValidationService memberValidationService) {
        this.articleService = articleService;
        this.memberValidationService = memberValidationService;
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<String> createArticle(
            @RequestHeader("X-User-Id") Long userId,  // API Gateway에서 전달된 인증된 사용자 ID
            @RequestBody ArticleRequestDto requestDto
    ) {
        // 멤버 검증
        if (!memberValidationService.isMemberValid(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 응답
        }

        // 게시글 저장
        articleService.createArticle(userId, requestDto);
        return ResponseEntity.ok("Article saved successfully");
    }

    // 게시글 조회
    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getAllArticle() {
        List<ArticleResponseDto> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public ResponseEntity<String> deleteArticle(
            @RequestHeader("X-User-Id") Long memberId,  // API Gateway에서 전달된 인증된 사용자 ID
            @PathVariable Long articleId
    ) {
        // 멤버 검증
        if (!memberValidationService.isMemberValid(memberId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 응답
        }

        articleService.deleteArticleByUserId(memberId, articleId);
        return ResponseEntity.ok("Article deleted successfully");
    }
}
