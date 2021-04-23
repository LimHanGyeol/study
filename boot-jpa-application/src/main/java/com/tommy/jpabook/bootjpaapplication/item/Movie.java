package com.tommy.jpabook.bootjpaapplication.item;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@DiscriminatorValue(value = "Movie")
@Entity
public class Movie extends Item {

    private String director;
    private String actor;
}
