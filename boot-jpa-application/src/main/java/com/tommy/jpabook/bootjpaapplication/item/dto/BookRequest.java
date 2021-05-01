package com.tommy.jpabook.bootjpaapplication.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

}
