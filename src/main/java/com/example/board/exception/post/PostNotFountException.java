package com.example.board.exception.post;

import com.example.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class PostNotFountException extends ClientErrorException {

    public PostNotFountException() {
        super(HttpStatus.NOT_FOUND, "Post not found");
    }

    public PostNotFountException(Long postId) {
        super(HttpStatus.NOT_FOUND, "Post with postId : " +  postId + " not found");
    }

    public PostNotFountException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
