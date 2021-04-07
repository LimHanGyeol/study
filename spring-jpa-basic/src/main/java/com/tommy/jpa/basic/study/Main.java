package com.tommy.jpa.basic.study;

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
            Team teamBlue = new Team("청팀");
            entityManager.persist(teamBlue);

            Member member = new Member("임한결");
            member.addTeamId(teamBlue);
            entityManager.persist(member);

            Member findMember = entityManager.find(Member.class, member.getId());// 1차 캐시로 인해 Select 쿼리가 발생하지 않음
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("Main.main" + m);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
