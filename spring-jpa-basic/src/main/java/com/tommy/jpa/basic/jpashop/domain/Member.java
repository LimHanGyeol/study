package com.tommy.jpa.basic.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;
    private String city;
    private String street;
    private String zipCode;

    // 보통 주문 Application에서도 주문 테이블의 MEMBER_ID로 통계를 뽑아낸다.
    // 양방향 관계는 권장되지 않지만 실습을 위해 양방향으로 설정 한다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
