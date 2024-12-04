package board.board.controller;

import board.board.dto.ArticleDto;
import board.board.entity.Article;
import board.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> createArticle(
            @RequestBody ArticleDto articleDto,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7); // "Bearer " 제거
        Article article = articleService.createArticle(token, articleDto);
        return ResponseEntity.ok(article);
    }
}
