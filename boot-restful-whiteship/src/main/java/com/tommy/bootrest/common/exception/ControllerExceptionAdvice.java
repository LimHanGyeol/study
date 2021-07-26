package com.tommy.bootrest.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({NonExistResourceException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseV1> nonExistResourceExceptionHandler(NonExistResourceException e) {
        ErrorResponseV1 errorResponse = ErrorResponseV1.of(e.getMessage(), NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseV1> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponseV1 errorResponse = ErrorResponseV1.of(e.getMessage(), BAD_REQUEST.value(), e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
