package com.tommy.bootrestful.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ExceptionResponse {

    private String message;
    private String details;
    private LocalDateTime timeStamp;

    public ExceptionResponse(String message, String details, LocalDateTime timeStamp) {
        this.message = message;
        this.details = details;
        this.timeStamp = timeStamp;
    }
}
