package com.tommy.jpabook.bootjpaapplication.order;

import com.tommy.jpabook.bootjpaapplication.delivery.Delivery;
import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
public class Order {

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
}
