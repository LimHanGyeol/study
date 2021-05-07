package com.tommy.jpabook.bootjpaapplication.order.dto;

import com.tommy.jpabook.bootjpaapplication.member.domain.Address;
import com.tommy.jpabook.bootjpaapplication.order.domain.Order;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemResponse> orderItems;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.name = order.getOrderedMemberName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDeliveryAddress();
        this.orderItems = getOrderItemResponses(order);
    }

    private List<OrderItemResponse> getOrderItemResponses(Order order) {
        return order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }
}
