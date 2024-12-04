package board.board.service;

import board.board.dto.ArticleDto;
import board.board.entity.Article;
import board.board.repository.ArticleRepository;
import board.board.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final JwtUtil jwtUtil;

    public Article createArticle(String token, ArticleDto articleDto) {
        String usernameFromToken = jwtUtil.extractUsername(token);

        // JWT에서 추출한 username과 요청 writer 비교
        if (!usernameFromToken.equals(articleDto.getWriter())) {
            throw new IllegalArgumentException("Writer does not match authenticated user");
        }

        Article article = Article.builder()
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .writer(articleDto.getWriter())
                .build();

        return articleRepository.save(article);
    }

}
