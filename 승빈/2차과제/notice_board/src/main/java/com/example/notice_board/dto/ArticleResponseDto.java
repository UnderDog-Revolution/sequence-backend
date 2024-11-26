package com.example.notice_board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long writer;
}
