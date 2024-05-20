package com.example.postgres.exception;

import com.example.postgres.dto.ExceptionMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionMessageDto> handleResourceNotFoundException(ResourceNotFoundException nfe) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionMessageDto(nfe.getMessage()));
    }
}
