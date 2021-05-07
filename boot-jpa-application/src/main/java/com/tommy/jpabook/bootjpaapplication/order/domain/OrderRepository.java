package com.tommy.jpabook.bootjpaapplication.order.domain;

import com.tommy.jpabook.bootjpaapplication.order.dto.SimpleOrderQueryDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long save(Order order) {
        entityManager.persist(order);
        return order.getId();
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        return entityManager.createQuery("SELECT o FROM Order o join o.member m " +
                "WHERE o.status = :status " +
                "AND m.name LIKE :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) // 최대 1000건
                .getResultList();
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        boolean isFirstCondition = true;
        String jpql = "SELECT o FROM Order o JOIN o.member m";

        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " WHERE";
                isFirstCondition = false;
            } else {
                jpql += " AND";
            }
            jpql += " o.status = :status";
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " WHERE";
                isFirstCondition = false;
            } else {
                jpql += " AND";
            }
            jpql += " m.name LIKE :name";
        }

        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return entityManager.createQuery(
                "SELECT o FROM Order  o" +
                        " JOIN FETCH o.member m" +
                        " JOIN FETCH o.delivery d", Order.class
        ).getResultList();
    }

    public List<SimpleOrderQueryDto> findOrderDtos() {
        return entityManager.createQuery(
                "SELECT NEW com.tommy.jpabook.bootjpaapplication.order.dto.SimpleOrderQueryDto(o.id, m.name, o.orderDate, o.status, m.address) " +
                        " FROM Order o " +
                        " JOIN o.member m " +
                        " JOIN o.delivery d", SimpleOrderQueryDto.class
        ).getResultList();
    }
}
