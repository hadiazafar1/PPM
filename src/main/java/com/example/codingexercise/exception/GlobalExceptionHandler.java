package com.example.codingexercise.exception;
import java.util.NoSuchElementException;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle IllegalArgumentException
    @ExceptionHandler
    public APIError handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(Response.SC_BAD_REQUEST)
                .body(new APIError(ex.getMessage(), Response.SC_BAD_REQUEST))
                .getBody();

    }

    // Handle NoSuchElementException
    @ExceptionHandler
    public APIError handleNotFoundException(NoSuchElementException ex) {
        return ResponseEntity.status(Response.SC_NOT_FOUND)
                .body(new APIError(ex.getMessage(), Response.SC_NOT_FOUND))
                .getBody();
    }

}
