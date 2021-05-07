package com.tommy.jpabook.bootjpaapplication.order.domain;

import com.tommy.jpabook.bootjpaapplication.delivery.Delivery;
import com.tommy.jpabook.bootjpaapplication.delivery.DeliveryStatus;
import com.tommy.jpabook.bootjpaapplication.member.domain.Address;
import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order {

    private static final String INVALID_CANCEL_ORDER = "이미 출고 완료된 상품은 주문 취소가 불가능 합니다.";

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 : ORDER, CANCEL

    public Order(LocalDateTime orderDate, OrderStatus status) {
        this.orderDate = orderDate;
        this.status = status;
    }

    public static Order createOrder(List<OrderItem> orderItems) {
        Order order = new Order(LocalDateTime.now(), OrderStatus.ORDER);
        orderItems.forEach(order::addOrderItem);
        return order;
    }

    public void orderedMember(Member member) {
        this.member = member;
        member.addOrder(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.ofOrder(this);
    }

    public void requestDelivery(Delivery delivery) { // 주문 할 경우 배송 신청
        this.delivery = delivery;
        delivery.ofOrder(this);
    }

    public void cancelOrder() {
        if (delivery.checkStatus(DeliveryStatus.COMP)) {
            throw new IllegalStateException(INVALID_CANCEL_ORDER);
        }
        this.status = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    public Address getDeliveryAddress() {
        return delivery.getAddress();
    }

    public String getOrderedMemberName() {
        return member.getName();
    }
}
