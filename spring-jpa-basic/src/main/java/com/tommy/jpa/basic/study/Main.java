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

            Team teamRed = new Team("적팀");
            entityManager.persist(teamRed);

            Member member = new Member("임한결");
            member.addTeamId(teamBlue);
            entityManager.persist(member);

            entityManager.find(Member.class, member.getId()); // 1차 캐시로 인해 Select 쿼리가 발생하지 않음

            Team findTeamRed = entityManager.find(Team.class, teamRed.getId());
            member.addTeamId(findTeamRed); // 더티체킹


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
