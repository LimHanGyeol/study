package com.tommy.jpa.basic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        try {
//             Member 객체 Select 및 Update
//             Member member = entityManager.find(Member.class, 1L);
//             member.update("tommy");

            List<Member> results = entityManager.createQuery("SELECT m FROM Member ", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
