package com.example.board.exception.reply;

import com.example.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class ReplyNotFountException extends ClientErrorException {

    public ReplyNotFountException() {
        super(HttpStatus.NOT_FOUND, "reply not found");
    }

    public ReplyNotFountException(Long replyId) {
        super(HttpStatus.NOT_FOUND, "reply with replyId : " +  replyId + " not found");
    }

    public ReplyNotFountException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
