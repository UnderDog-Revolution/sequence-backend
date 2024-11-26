package com.example.notice_board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class ArticleRequestDto {
    private String title;
    private String content;
}
