package com.tommy.jpabook.bootjpaapplication.order.service;

import com.tommy.jpabook.bootjpaapplication.delivery.Delivery;
import com.tommy.jpabook.bootjpaapplication.item.domain.Item;
import com.tommy.jpabook.bootjpaapplication.item.service.ItemService;
import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import com.tommy.jpabook.bootjpaapplication.member.service.MemberService;
import com.tommy.jpabook.bootjpaapplication.order.domain.Order;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderItem;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberService.findById(memberId);
        Item item = itemService.findById(itemId);

        Delivery delivery = new Delivery(member.getAddress());
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(List.of(orderItem));
        order.orderedMember(member);
        order.requestDelivery(delivery);

        orderRepository.save(order);
        return order.getId();
    }

    public void cancelOrder(Long orderId) {
        Order order = findById(orderId);
        order.cancelOrder();
    }

    @Transactional(readOnly = true)
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 주문 정보입니다. 다시 한번 확인해주세요."));
    }
}
