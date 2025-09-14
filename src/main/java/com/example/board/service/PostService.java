package com.example.board.service;

import com.example.board.exception.post.PostNotFountException;
import com.example.board.exception.user.UserNotAllowedException;
import com.example.board.exception.user.UserNotFoundException;
import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostPatchRequestBody;
import com.example.board.model.post.PostRequestBody;
import com.example.board.model.entity.PostEntity;
import com.example.board.repository.PostEntityRepository;
import com.example.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired private PostEntityRepository postEntityRepository;

    @Autowired private UserEntityRepository userEntityRepository;

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

    public Post createPost(PostRequestBody body, UserEntity user) {

        var savedPostEntity = postEntityRepository.save(
                PostEntity.of(body.body(),  user)
        );

        return Post.from(savedPostEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody body, UserEntity user) {

        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));

        if(!postEntity.getUser().equals(user)) {
            throw new UserNotAllowedException();
        }

        postEntity.setBody(body.body());
        var updatedPostEntity = postEntityRepository.save(postEntity);

        return Post.from(updatedPostEntity);
    }

    public void deletePost(Long postId, UserEntity user) {

        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));

        if(!postEntity.getUser().equals(user)) {
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostsByUsername(String username) {

        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var posts = postEntityRepository.findByUser(userEntity);

        return posts.stream()
                .map(Post::from)
                .toList();
    }
}
