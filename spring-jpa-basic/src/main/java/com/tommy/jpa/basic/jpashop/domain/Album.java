package com.tommy.jpa.basic.jpashop.domain;

import javax.persistence.Entity;

@Entity
public class Album extends Item {

    private String artist;
    private String etc;

    public Album() {
    }

    public Album(String artist, String etc) {
        this.artist = artist;
        this.etc = etc;
    }

    public String getArtist() {
        return artist;
    }

    public String getEtc() {
        return etc;
    }
}
