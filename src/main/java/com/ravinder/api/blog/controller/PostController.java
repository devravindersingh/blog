package com.ravinder.api.blog.controller;

import com.ravinder.api.blog.entity.Post;
import com.ravinder.api.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Post createPost(@RequestBody Post post){
        return postService.createPost(post);
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id){
        return postService.getPost(id);

    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postService.getAllPost();
    }


    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post){
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id){
        postService.deletePost(id);
    }
}
