package com.tommy.jpa.basic.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 테이블 이름을 ORDERS 로 지정한다.
 * 이유는 ORDER 를 예약어로 사용하고 있는 DB 가 있기 때문이다.
 */
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
