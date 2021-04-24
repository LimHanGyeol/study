package com.tommy.jpabook.bootjpaapplication.item.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long save(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);
            return item.getId();
        }
        entityManager.merge(item);
        return item.getId();
    }

    public Item findById(Long itemId) {
        return entityManager.find(Item.class, itemId);
    }

    public List<Item> findAll() {
        return entityManager.createQuery("SELECT i FROM Item AS i", Item.class)
                .getResultList();
    }
}
