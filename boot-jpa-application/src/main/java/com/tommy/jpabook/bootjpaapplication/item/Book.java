package com.tommy.jpabook.bootjpaapplication.item;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@DiscriminatorValue(value = "Book")
@Entity
public class Book extends Item {

    private String author;
    private String isbn;
}
