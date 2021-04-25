package com.tommy.jpabook.bootjpaapplication.order.domain;

import com.tommy.jpabook.bootjpaapplication.item.domain.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    public OrderItem(Item item, int orderPrice, int count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        item.removeStockQuantity(count);
        return new OrderItem(item, orderPrice, count);
    }

    public void ofOrder(Order order) {
        this.order = order;
    }

    public void cancel() { // 재고 수량을 원복해준다.
        item.addStockQuantity(count);
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
}
