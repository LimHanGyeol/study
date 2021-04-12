package com.tommy.jpa.basic.jpql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * JPQL 학습용 클래스이다.
 * 한 프로젝트에서 모든 예제를 관리하기 때문에 suffix 로 J를 붙였다.
 */
@Entity
public class ProductJ {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int price;
    private int stockAmount;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStockAmount() {
        return stockAmount;
    }
}
