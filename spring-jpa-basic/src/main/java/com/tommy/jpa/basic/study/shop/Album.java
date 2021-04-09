package com.tommy.jpa.basic.study.shop;

import javax.persistence.Entity;

@Entity
public class Album extends Items {

    private String artist;

    public Album() {
    }

    public Album(String name, int price, String artist) {
        super(name, price);
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }
}
