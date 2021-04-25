package com.tommy.jpabook.bootjpaapplication.item.exception;

public class NotEnoughStockException extends RuntimeException {

    private static final String MESSAGE_NOT_ENOUGH_STOCK = "need more stock";

    public NotEnoughStockException() {
        super(MESSAGE_NOT_ENOUGH_STOCK);
    }
}
