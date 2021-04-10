package com.tommy.jpa.basic.study;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        try {
            Team teamBlue = new Team("청팀");
            entityManager.persist(teamBlue);

             Member member = new Member("임한결");
            // 양방향 관계 설정 시에는 두 객체에 값을 설정해줘야 한다.
             member.joinTeam(teamBlue);
             entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

            Member referenceMember = entityManager.find(Member.class, member.getId());

            System.out.println("referenceMember = " + referenceMember.getTeam().getClass());

            System.out.println("=============");
            referenceMember.getTeam(); // 프록시를 가져오는 거라서 쿼리가 나가지 않음.
            System.out.println("team = " + referenceMember.getTeam().getName()); // 초기화

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
