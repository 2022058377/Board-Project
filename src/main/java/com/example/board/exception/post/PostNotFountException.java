package com.example.board.exception.post;

import com.example.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class PostNotFountException extends ClientErrorException {

    public PostNotFountException() {
        super(HttpStatus.NOT_FOUND, "Post not fount");
    }

    public PostNotFountException(Long postId) {
        super(HttpStatus.NOT_FOUND, "Post wiht postId : " +  postId + " not fount");
    }

    public PostNotFountException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
