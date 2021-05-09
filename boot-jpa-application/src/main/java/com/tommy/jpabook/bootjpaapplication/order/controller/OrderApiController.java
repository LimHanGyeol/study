package com.tommy.jpabook.bootjpaapplication.order.controller;

import com.tommy.jpabook.bootjpaapplication.order.domain.Order;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderItem;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderRepository;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderSearch;
import com.tommy.jpabook.bootjpaapplication.order.dto.OrderResponse;
import com.tommy.jpabook.bootjpaapplication.order.dto.SimpleOrderQueryDto;
import com.tommy.jpabook.bootjpaapplication.order.dto.SimpleOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * XToOne
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderResponse> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch()); // N + 1 문제 발생
        List<SimpleOrderResponse> orderResponses = orders.stream()
                .map(SimpleOrderResponse::new)
                .collect(Collectors.toList());
        return orderResponses;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderResponse> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderResponse> orderResponses = orders.stream()
                .map(SimpleOrderResponse::new)
                .collect(Collectors.toList());
        return orderResponses;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersV4() {
        return orderRepository.findOrderDtos();
    }

    @GetMapping("/api/v1/orders")
    public List<Order> orderItemsV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        for (Order order : orders) {
            order.getOrderedMemberName();
            order.getDeliveryAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> orderItem.getItem().getName());
        }
        return orders;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderResponse> orderItemsV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
        return orderResponses;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderResponse> orderItemsV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
        return orderResponses;
    }
}
