package com.example.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final PostRepository postRepository;
    @GetMapping("/posts")

    public String getPosts(Model model) {
        List<Post> posts = postRepository.findAll();
        System.out.println(posts);
        model.addAttribute("posts", posts);
        return "posts.html";
    }

    @GetMapping("/posts/{id}")
    public String getPost(Model model, @PathVariable Integer id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            System.out.println(post.get());
            model.addAttribute("post", post.get());
            return "post.html";
        } else {
            return "redirect:/posts";
        }
    }

    @GetMapping("/create")
    public String getCreate() {
        return "create.html";
    }

    @PostMapping("/create")
    public String createPost(@RequestParam String title, @RequestParam String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        postRepository.save(post);

        return "redirect:/posts";
    }
}
