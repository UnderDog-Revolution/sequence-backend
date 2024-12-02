package board.board.controller;

import board.board.dto.PostDto;
import board.board.entity.Post;
import board.board.service.PostService;
import board.board.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostDto postDto, HttpServletRequest request) {
        // JWT 토큰에서 username 추출
        String token = extractToken(request);
        String username = jwtUtil.extractUsername(token);

        Post post = postService.createPost(postDto, username);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto, @RequestParam String username) {
        return ResponseEntity.ok(postService.updatePost(postId, postDto, username));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, @RequestParam String username) {
        postService.deletePost(postId, username);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword) {
        return ResponseEntity.ok(postService.searchPosts(keyword));
    }

    private static final Logger logger = Logger.getLogger(PostController.class.getName());

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.fine("Authorization Header: " + bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("Invalid or missing Authorization header. Please include a valid Bearer token.");
    }

}

