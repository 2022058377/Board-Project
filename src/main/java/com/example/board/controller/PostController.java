package com.example.board.controller;

import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostPatchRequestBody;
import com.example.board.model.post.PostRequestBody;
import com.example.board.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @Autowired private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Authentication authentication) {
        log.info("GET /api/v1/posts");
        return ResponseEntity.ok(postService.getPosts((UserEntity) authentication.getPrincipal()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(
            @PathVariable Long postId,
            Authentication authentication
    ) {

        log.info("GET /api/v1/posts/{}", postId);
        var post = postService.getPostByPostId(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody PostRequestBody body,
            Authentication authentication
            ) {

        var post = postService.createPost(body, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostPatchRequestBody body,
            Authentication authentication
    ) {

        log.info("PATCH /api/v1/posts/{}", postId);
        var post = postService.updatePost(postId, body, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {

        log.info("DELETE /api/v1/posts/{}", postId);
        postService.deletePost(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(
            @PathVariable Long postId,
            Authentication authentication
    ) {

        var post = postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }
}
