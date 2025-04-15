package com.ravinder.api.blog.service;

import com.ravinder.api.blog.entity.Post;
import com.ravinder.api.blog.repository.PostRepository;
import com.ravinder.api.blog.scheduler.Task;
import com.ravinder.api.blog.scheduler.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TaskScheduler taskScheduler;

    public Post createPost(Post post) {
        taskScheduler.scheduleTask(new Task(
                "create-post-" + System.currentTimeMillis(),
                "Save post to DB",
                1,
                () -> postRepository.save(post)
        ));
        return post;
    }

    @Cacheable(value = "posts", key = "#id")
    public Post getPost(Long id){
        logger.info("Fetching post with ID {} from database", id); // Log before DB call
        Post post =  postRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Post not found"));
        logger.info("Post with ID {} retrieved from database", id); // Log after DB call
        return post;
    }

    @Cacheable(value = "postList")
    public List<Post> getAllPost(){
        logger.info("fetching all posts from database");
        List<Post> posts = postRepository.findAll();
        logger.info("retrieved {} posts from database" , posts.size());
        return posts;
    }

    @CacheEvict(value = "posts", key = "#id")
    public Post updatePost(Long id, Post updatedPost){
        Post exisitingPost = getPost(id); // Fetches from cache if available
        exisitingPost.setAuthor(updatedPost.getAuthor());
        exisitingPost.setContent(updatedPost.getContent());
        exisitingPost.setTitle(updatedPost.getTitle());
        taskScheduler.scheduleTask(new Task(
                "update-post - " + id,
                "update post in DB",
                1,
                () -> postRepository.save(exisitingPost)
        ));
        return exisitingPost;
    }

    @CacheEvict(value = {"posts", "postList"}, allEntries = true)
    public void deletePost(Long id) {
        taskScheduler.scheduleTask(new Task(
                "delete-post - " + id,
                "delete post from DB",
                1,
                () -> postRepository.deleteById(id)
        ));
    }

}
