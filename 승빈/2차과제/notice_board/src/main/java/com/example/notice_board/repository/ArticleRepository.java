package com.example.notice_board.repository;

import com.example.notice_board.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    // 작성자 ID와 게시글 ID로 게시글 삭제
    void deleteByWriterIdAndId(Long writerId, Long id);
}
