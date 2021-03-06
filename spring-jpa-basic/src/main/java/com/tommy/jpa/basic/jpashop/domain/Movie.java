package com.tommy.jpa.basic.jpashop.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 상속관계의 상위테이블인 ITEMS의 DTYPE에 값을 넣을 경우,
 * 기본 값은 Entity Name 이다.
 * 이를 바꾸고 싶을 경우 DiscriminatorValue로 설정해 줄 수 있다.
 */
@Entity
@DiscriminatorValue("A")
public class Movie extends Item {

    private String director;
    private String actor;

    protected Movie() {
    }

    public Movie(String director, String actor) {
        this.director = director;
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public String getActor() {
        return actor;
    }
}
