package com.tommy.jpa.basic.study.shop;

import javax.persistence.Entity;

@Entity
public class Book extends Items {

    private String author;
    private String isbn;

    public Book() {
    }

    public Book(String author, String isbn) {
        this.author = author;
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }
}
