package board.board.controller;

import board.board.dto.CommentDto;
import board.board.entity.Comment;
import board.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<Comment> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto, @RequestParam String username) {
        Comment comment = commentService.createComment(postId, commentDto, username);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @RequestParam String username) {
        commentService.deleteComment(commentId, username);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
