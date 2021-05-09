package com.tommy.jpabook.bootjpaapplication.order.domain;

import com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderItemQueryDto;
import com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> results = findOrders();

        List<Long> orderIds = toOrderIds(results);
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        results.forEach(orderQueryDto -> {
            Long orderId = orderQueryDto.getOrderId();
            orderQueryDto.addOrderItems(orderItemMap.get(orderId));
        });
        return results;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = entityManager.createQuery(
                "SELECT NEW com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " FROM OrderItem oi " +
                        " JOIN oi.item i " +
                        " WHERE oi.order.id IN :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
        return orderItemMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> results) {
        return results.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(Collectors.toList());
    }
}
