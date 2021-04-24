package com.tommy.jpabook.bootjpaapplication.item.domain;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@DiscriminatorValue(value = "Album")
@Entity
public class Album extends Item {

    private String artist;
    private String etc;
}
