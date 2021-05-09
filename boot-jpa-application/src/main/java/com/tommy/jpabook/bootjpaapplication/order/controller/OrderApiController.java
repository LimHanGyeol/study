package com.tommy.jpabook.bootjpaapplication.order.controller;

import com.tommy.jpabook.bootjpaapplication.order.domain.*;
import com.tommy.jpabook.bootjpaapplication.order.dto.OrderResponse;
import com.tommy.jpabook.bootjpaapplication.order.dto.SimpleOrderResponse;
import com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderFlatDto;
import com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderItemQueryDto;
import com.tommy.jpabook.bootjpaapplication.order.dto.query.OrderQueryDto;
import com.tommy.jpabook.bootjpaapplication.order.dto.query.SimpleOrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final OrderQueryRepository orderQueryRepository;

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

    @GetMapping("/api/v3.1/orders")
    public List<OrderResponse> orderItemsV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                 @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
        return orderResponses;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> orderItemsV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> orderItemsV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> orderItemsV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(Collectors.groupingBy(orderFlatDto -> new OrderQueryDto(orderFlatDto.getOrderId(), orderFlatDto.getName(), orderFlatDto.getOrderDate(), orderFlatDto.getOrderStatus(), orderFlatDto.getAddress()),
                        Collectors.mapping(orderFlatDto -> new OrderItemQueryDto(orderFlatDto.getOrderId(), orderFlatDto.getItemName(), orderFlatDto.getOrderPrice(), orderFlatDto.getCount()), Collectors.toList())
                )).entrySet().stream()
                .map(entry -> new OrderQueryDto(entry.getKey().getOrderId(), entry.getKey().getName(), entry.getKey().getOrderDate(), entry.getKey().getOrderStatus(), entry.getKey().getAddress(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
