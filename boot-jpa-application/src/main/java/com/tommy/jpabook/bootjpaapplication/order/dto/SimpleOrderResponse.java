package com.tommy.jpabook.bootjpaapplication.order.dto;

import com.tommy.jpabook.bootjpaapplication.member.domain.Address;
import com.tommy.jpabook.bootjpaapplication.order.domain.Order;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SimpleOrderResponse {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderResponse(Order order) {
        this.orderId = order.getId();
        this.name = order.getOrderedMemberName(); // Lazy 초기화
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDeliveryAddress(); // Lazy 초기화
    }
}
