package com.tommy.jpabook.bootjpaapplication.order.dto;

import com.tommy.jpabook.bootjpaapplication.order.domain.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemResponse {

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemResponse(OrderItem orderItem) {
        this.itemName = orderItem.getItemName();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }
}
