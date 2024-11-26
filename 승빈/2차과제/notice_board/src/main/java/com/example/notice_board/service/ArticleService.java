package com.example.notice_board.service;

import com.example.notice_board.dto.ArticleRequestDto;
import com.example.notice_board.dto.ArticleResponseDto;
import com.example.notice_board.entity.Article;
import com.example.notice_board.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // 게시글 작성
    @Transactional
    public ArticleResponseDto createArticle(Long UserId, ArticleRequestDto requestDto) {
        Article article = new Article();
        article.setTitle(requestDto.getTitle());
        article.setContent(requestDto.getContent());
        article.setWriterId(UserId);
        articleRepository.save(article);

        return mapToResponseDto(article);
    }

    // 게시글 목록 조회
    public List<ArticleResponseDto> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    // DTO 변환 메서드
    private ArticleResponseDto mapToResponseDto(Article article) {
        ArticleResponseDto responseDto = new ArticleResponseDto();
        responseDto.setId(article.getId());
        responseDto.setTitle(article.getTitle());
        responseDto.setContent(article.getContent());
        responseDto.setWriter(article.getWriterId());
        return responseDto;
    }

    @Transactional
    public void deleteArticleByUserId(Long memberId, Long articleId) {
        articleRepository.deleteByWriterIdAndId(memberId, articleId);
    }
}
