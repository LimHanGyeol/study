package com.tommy.jpa.basic;

import com.tommy.jpa.basic.jpashop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpqlMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        try {
            // createJPQL(entityManager);
            // createCriteria(entityManager);
            // createNativeQuery(entityManager);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }

    private static void createNativeQuery(EntityManager entityManager) {
        Member member = new Member("Tommy");
        entityManager.persist(member);

        // flush 는 commit과 query를 호출할 때 적용된다.
        // 밑의 네이티브 쿼리를 수행할 때 flush가 되고 쿼리가 호출된다.
        // Spring에서 dbconnection 을 얻어오거나 하여 사용할 경우에는 JPA와 연관이 없다.
        // 그래서 flush도 적용이 되지 않아 결과가 제대로 조회되지 않는다.
        // JDBCTemplate 및 DBConnection 을 사용할 경우에는 강제로 flush를 해줘야 한다.
        List<Member> results = entityManager.createNativeQuery(
                "SELECT MEMBER_ID, city, street, zipcode, name FROM MEMBER", Member.class
        ).getResultList();

        results.forEach(System.out::println);
    }

    private static void createCriteria(EntityManager entityManager) {
        // Criteria가 표준 스펙이여서 설명은 하지만 실무에서 잘 사용되진 않는다.
        // 이유는 유지보수가 쉽지 않기 때문이다.
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> dynamicQuery = criteriaBuilder.createQuery(Member.class);

        Root<Member> member = dynamicQuery.from(Member.class);

        CriteriaQuery<Member> criteriaQuery = dynamicQuery.select(member);

        String name = "testName";
        if (name != null) {
            criteriaQuery.where(criteriaBuilder.equal(member.get("name"), "kim"));
        }

        List<Member> results = entityManager.createQuery(criteriaQuery)
                .getResultList();
    }

    private static void createJPQL(EntityManager entityManager) {
        List<Member> results = entityManager.createQuery(
                "SELECT m FROM Member AS m WHERE m.name LIKE '%kim'", Member.class
        ).getResultList();

        for (Member member : results) {
            System.out.println("member = " + member);
        }
    }
}
