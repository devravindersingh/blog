package com.ravinder.api.blog.repository;

import com.ravinder.api.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
