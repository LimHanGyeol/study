package com.tommy.jpa.basic.jpql;

import javax.persistence.*;

/**
 * JPQL 학습용 클래스이다.
 * 한 프로젝트에서 모든 예제를 관리하기 때문에 suffix 로 J를 붙였다.
 *
 * Order의 경우 Order가 각 DB마다 예약어로 등록되어 있는 경우가 있어 문제가 생길 수 있다.
 * 이런 경우 관례상 Orders 로 명명한다.
 * 기존에 이미 Orders로 만든 테이블이 있어, OrderJ를 유지한다.
 */
@Entity
public class OrderJ {

    @Id
    @GeneratedValue
    private Long id;
    private int orderAmount;

    @Embedded
    private AddressJ address;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private ProductJ product;

    public Long getId() {
        return id;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public AddressJ getAddress() {
        return address;
    }

    public ProductJ getProduct() {
        return product;
    }
}
