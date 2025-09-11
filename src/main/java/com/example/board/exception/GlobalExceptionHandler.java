package com.example.board.exception;

import com.example.board.model.error.ClientErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ClientErrorResponse> handleException(ClientErrorException ex) {

        return new ResponseEntity<>(
                new ClientErrorResponse(ex.getStatus(), ex.getMessage()),
                ex.getStatus());
    }
}
