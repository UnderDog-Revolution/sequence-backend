package com.example.SequenceBoard.service;

import com.example.SequenceBoard.dto.ArticleRequestDto;
import com.example.SequenceBoard.dto.ArticleResponseDto;
import com.example.SequenceBoard.entity.Article;
import com.example.SequenceBoard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArticleService {
    
    private final ArticleRepository articleRepository;
    
    @Transactional
    public ArticleResponseDto createArticle(ArticleRequestDto requestDto) {
        Article article = new Article();
        article.setTitle(requestDto.getTitle());
        article.setContent(requestDto.getContent());
        article.setWriter(requestDto.getUsername());
        article.setCreatedAt(LocalDateTime.now());
        
        Article savedArticle = articleRepository.save(article);
        return new ArticleResponseDto(savedArticle);
    }
} 