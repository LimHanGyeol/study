package com.tommy.jpabook.bootjpaapplication.order.dto.query;

import com.tommy.jpabook.bootjpaapplication.member.domain.Address;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    public void addOrderItems(List<OrderItemQueryDto> orderItems) {
        this.orderItems = orderItems;
    }
}
