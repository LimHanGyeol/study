package com.tommy.jpa.basic.jpashop.domain;

import javax.persistence.*;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DELIVERY_ID")
    private Long id;

    @Embedded
    private Address address;

    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    public Long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public Order getOrder() {
        return order;
    }
}
