package board.board.service;

import board.board.dto.CommentDto;
import board.board.entity.Comment;
import board.board.entity.Post;
import board.board.entity.User;
import board.board.repository.CommentRepository;
import board.board.repository.PostRepository;
import board.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Comment createComment(Long postId, CommentDto commentDto, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .post(post)
                .user(user)
                .build();

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findAll();
    }

    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Unauthorized to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
