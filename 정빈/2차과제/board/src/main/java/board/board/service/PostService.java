package board.board.service;

import board.board.dto.PostDto;
import board.board.entity.Post;
import board.board.entity.User;
import board.board.repository.PostRepository;
import board.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(PostDto postDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .user(user)
                .build();

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public Post updatePost(Long postId, PostDto postDto, String username) {
        Post post = getPostById(postId);

        if (!post.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Unauthorized to update this post");
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        return postRepository.save(post);
    }

    public void deletePost(Long postId, String username) {
        Post post = getPostById(postId);

        if (!post.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Unauthorized to delete this post");
        }

        postRepository.delete(post);
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.searchPostsByKeyword(keyword);
    }
}
