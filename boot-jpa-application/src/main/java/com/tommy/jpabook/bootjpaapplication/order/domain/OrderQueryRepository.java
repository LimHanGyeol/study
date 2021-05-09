package com.tommy.jpabook.bootjpaapplication.order.domain;

import com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderItemQueryDto;
import com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final EntityManager entityManager;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> results = findOrders();

        results.forEach(orderQueryDto -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(orderQueryDto.getOrderId());
            orderQueryDto.addOrderItems(orderItems);
        });
        return results;
    }

    private List<OrderQueryDto> findOrders() {
        return entityManager.createQuery(
                "SELECT NEW com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " FROM Order o " +
                        " JOIN o.member m " +
                        " JOIN o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return entityManager.createQuery(
                "SELECT NEW com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " FROM OrderItem oi " +
                        " JOIN oi.item i " +
                        " WHERE oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
