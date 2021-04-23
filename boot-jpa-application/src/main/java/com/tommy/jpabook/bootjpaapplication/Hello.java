package com.tommy.jpabook.bootjpaapplication;

import lombok.Getter;

@Getter
public class Hello {

    private final String hello;

    public Hello(String hello) {
        this.hello = hello;
    }
}
