package com.tommy.jpabook.bootjpaapplication.order.domain;

import lombok.Getter;

@Getter
public class OrderSearch {

    private final String memberName;
    private final OrderStatus orderStatus;

    public OrderSearch(String memberName, OrderStatus orderStatus) {
        this.memberName = memberName;
        this.orderStatus = orderStatus;
    }
}
