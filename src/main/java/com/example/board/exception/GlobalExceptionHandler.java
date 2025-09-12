package com.example.board.exception;

import com.example.board.model.error.ClientErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ClientErrorResponse> handleException(MethodArgumentNotValidException ex) {

        var errorMessage = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList()
                .toString();

        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ClientErrorResponse> handleException(HttpMessageNotReadableException ex) {

        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ClientErrorResponse> handleException(RuntimeException ex) {

        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientErrorResponse> handleException(Exception ex) {

        return ResponseEntity.internalServerError().build();
    }
}
