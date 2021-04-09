package com.tommy.jpa.basic.study.shop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ShopMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        try {
            Movie movie = new Movie("인셉션", 123, "aaa", "bbb");

            entityManager.persist(movie);

            entityManager.flush();
            entityManager.clear();

            Movie findMovie = entityManager.find(Movie.class, movie.getId());
            System.out.println("findMovie = " + findMovie);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }

//     DROP TABLE ITEMS;
//     DROP TABLE ITEM;
//     DROP TABLE ACCOUNT;
//     DROP TABLE ALBUM;
//     DROP TABLE BOOK;
//     DROP TABLE CATEGORY_ITEM;
//     DROP TABLE CATEGORY;
//     DROP TABLE DELIVERY;
//     DROP TABLE LOCKER;
//     DROP TABLE MEMBER;
//     DROP TABLE MOVIE;
//     DROP TABLE ORDERITEM;
//     DROP TABLE ORDERS;
//     DROP TABLE TEAM;
}
