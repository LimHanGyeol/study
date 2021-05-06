package com.tommy.jpabook.bootjpaapplication;

import com.tommy.jpabook.bootjpaapplication.delivery.Delivery;
import com.tommy.jpabook.bootjpaapplication.item.domain.Book;
import com.tommy.jpabook.bootjpaapplication.member.domain.Address;
import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import com.tommy.jpabook.bootjpaapplication.order.domain.Order;
import com.tommy.jpabook.bootjpaapplication.order.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.Arrays;

/**
 * 총 주문 2개
 * userA
 *  - JPA1 BOOK
 *  - JPA2 BOOK
 * userB
 *  - SPRING1 BOOK
 *  - SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initData1();
        initService.initData2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager entityManager;

        public void initData1() {
            Member member = createMember("한결", "서울", "테헤란로", "12359");
            entityManager.persist(member);

            Book book1 = createBook(member.getName(), "JPA1 BOOK", 10000, "12345", 100);
            entityManager.persist(book1);

            Book book2 = createBook(member.getName(), "JPA2 BOOK", 20000, "12346", 100);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = createOrder(member, orderItem1, orderItem2, delivery);
            entityManager.persist(order);
        }

        public void initData2() {
            Member member = createMember("타미", "경기", "해밀예당3로", "12348");
            entityManager.persist(member);

            Book book1 = createBook(member.getName(), "SPRING1 BOOK", 20000, "12998", 200);
            entityManager.persist(book1);

            Book book2 = createBook(member.getName(), "SPRING2 BOOK", 40000, "12999", 300);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = createDelivery(member);
            Order order = createOrder(member, orderItem1, orderItem2, delivery);
            entityManager.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Address address = new Address(city, street, zipcode);
            return new Member(name, address);
        }

        private Book createBook(String memberName, String name, int price, String isbn, int stockQuantity) {
            return new Book(name, price, stockQuantity, memberName, isbn);
        }

        private Delivery createDelivery(Member member) {
            return new Delivery(member.getAddress());
        }

        private Order createOrder(Member member, OrderItem orderItem1, OrderItem orderItem2, Delivery delivery) {
            Order order = Order.createOrder(Arrays.asList(orderItem1, orderItem2));
            order.orderedMember(member);
            order.requestDelivery(delivery);
            return order;
        }
    }
}
