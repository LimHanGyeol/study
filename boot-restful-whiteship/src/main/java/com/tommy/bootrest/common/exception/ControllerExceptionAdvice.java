package com.tommy.bootrest.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(NonExistResourceException.class)
    public ResponseEntity<ErrorResponseV1> nonExistResourceExceptionHandler(NonExistResourceException e) {
        ErrorResponseV1 errorResponse = ErrorResponseV1.of(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
