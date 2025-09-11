package com.example.board.service;

import com.example.board.exception.post.PostNotFountException;
import com.example.board.model.Post;
import com.example.board.model.PostPatchRequestBody;
import com.example.board.model.PostRequestBody;
import com.example.board.model.entity.PostEntity;
import com.example.board.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired private PostEntityRepository postEntityRepository;

    public List<Post> getPosts() {

        var postEntities = postEntityRepository.findAll();
        return postEntities.stream()
                .map(Post::from)
                .toList();
    }

    public Post getPostByPostId(Long postId) {

        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));

        return Post.from(postEntity);
    }

    public Post createPost(PostRequestBody body) {

        var postEntity = new PostEntity();
        postEntity.setBody(body.body());
        var savedPostEntity = postEntityRepository.save(postEntity);

        return Post.from(savedPostEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody body) {

        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));

        postEntity.setBody(body.body());
        var updatedPostEntity = postEntityRepository.save(postEntity);

        return Post.from(updatedPostEntity);
    }

    public void deletePost(Long postId) {

        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));
        postEntityRepository.delete(postEntity);
    }
}
