package com.tommy.bootrest.event.exception;

import com.tommy.bootrest.common.exception.NonExistResourceException;

public class EventNonExistException extends NonExistResourceException {

    public EventNonExistException(String message) {
        super(message);
    }
}
