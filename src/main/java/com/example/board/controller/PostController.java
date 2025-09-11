package com.example.board.controller;

import com.example.board.model.Post;
import com.example.board.model.PostPatchRequestBody;
import com.example.board.model.PostRequestBody;
import com.example.board.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @Autowired private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        log.info("GET /api/v1/posts");
        return ResponseEntity.ok(postService.getPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(
            @PathVariable Long postId
    ) {

        log.info("GET /api/v1/posts/{}", postId);
        var post = postService.getPostByPostId(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody PostRequestBody body
            ) {

        log.info("POST /api/v1/posts");
        var post = postService.createPost(body);
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostPatchRequestBody body
    ) {

        log.info("PATCH /api/v1/posts/{}", postId);
        var post = postService.updatePost(postId, body);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId
    ) {

        log.info("DELETE /api/v1/posts/{}", postId);
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
