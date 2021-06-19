package com.tommy.bootrestful.common.exception;

import com.tommy.bootrestful.user.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class CommonExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), request.getDescription(false), LocalDateTime.now());

        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ExceptionResponse> handleUserNotFoundException(Exception exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), request.getDescription(false), LocalDateTime.now());

        return ResponseEntity.status(NOT_FOUND).body(exceptionResponse);
    }
}
