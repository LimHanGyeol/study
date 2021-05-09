package com.tommy.jpabook.bootjpaapplication.order.dto.query;

import com.tommy.jpabook.bootjpaapplication.member.domain.Address;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, List<OrderItemQueryDto> orderItems) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderItems = orderItems;
    }

    public void addOrderItems(List<OrderItemQueryDto> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderQueryDto that = (OrderQueryDto) o;
        return Objects.equals(getOrderId(), that.getOrderId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getOrderDate(), that.getOrderDate()) && getOrderStatus() == that.getOrderStatus() && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getOrderItems(), that.getOrderItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getName(), getOrderDate(), getOrderStatus(), getAddress(), getOrderItems());
    }
}
