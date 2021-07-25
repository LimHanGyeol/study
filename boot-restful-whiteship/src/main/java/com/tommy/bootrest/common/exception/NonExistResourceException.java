package com.tommy.bootrest.common.exception;

public class NonExistResourceException extends RuntimeException {

    public NonExistResourceException(String message) {
        super(message);
    }
}
