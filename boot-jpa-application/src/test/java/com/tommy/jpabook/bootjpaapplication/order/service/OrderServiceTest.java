package com.tommy.jpabook.bootjpaapplication.order.service;

import com.tommy.jpabook.bootjpaapplication.item.domain.Book;
import com.tommy.jpabook.bootjpaapplication.item.exception.NotEnoughStockException;
import com.tommy.jpabook.bootjpaapplication.member.domain.Address;
import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import com.tommy.jpabook.bootjpaapplication.order.domain.Order;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("상품 주문")
    void order() {
        // given
        Member member = createMember();
        Book book = createBook();

        // when
        Long savedOrderId = orderService.order(member.getId(), book.getId(), 2);

        // then
        Order findOrder = orderService.findById(savedOrderId);

        assertThat(findOrder.getTotalPrice()).isEqualTo(20000);
        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(findOrder.getOrderItems()).hasSize(1);
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("주문 취소")
    void cancelOrder() {
        // given
        Member member = createMember();
        Book book = createBook();
        Long savedOrderId = orderService.order(member.getId(), book.getId(), 2);

        // when
        orderService.cancelOrder(savedOrderId);

        // then
        Order findOrder = orderService.findById(savedOrderId);

        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품 주문 시 재고 수량이 부족할 경우 예외 발생")
    void invalidProductStockQuantity() {
        // given
        Member member = createMember();
        Book book = createBook();

        // when & then
        assertThatExceptionOfType(NotEnoughStockException.class)
                .isThrownBy(() -> orderService.order(member.getId(), book.getId(), 11));
    }

    private Book createBook() {
        Book book = new Book("시골 JPA", 10000, 10, "김영한", "12345");
        entityManager.persist(book);
        return book;
    }

    private Member createMember() {
        Address address = new Address("서울", "테헤란로", "12338");
        Member member = new Member("임한결", address);
        entityManager.persist(member);
        return member;
    }
}
