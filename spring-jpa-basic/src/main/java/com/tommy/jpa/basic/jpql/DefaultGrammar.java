package com.tommy.jpa.basic.jpql;

import com.tommy.jpa.basic.jpashop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DefaultGrammar {

    public static void defaultGrammar() {
        // createJPQL(entityManager);
        // createCriteria(entityManager);
        // createNativeQuery(entityManager);
        // createDefaultJpqlAndQueryAPI(entityManager);

        // 프로젝션으로 반환한 객체도 영속성 컨텍스트의 관리 대상이 된다.
        // entityTypeProjection(entityManager);
        // embeddedTypeProjection(entityManager);
        // scalaTypeProjection(entityManager);

        // Paging
        // List<MemberJ> results = getPagingMember(entityManager);

        // Join
        // joinQuery(entityManager);

        // ENUM ValueType
        // jpqlEnum(entityManager);

        // 조건문 (CASE)
        // jpqlCondition(entityManager);

        // JPQL 기본 함수
        // defaultJPQL(entityManager);
    }

    private static void defaultJPQL(EntityManager entityManager) {
        // concat (Hibernate 지원 'a' || 'b')
        String query = "SELECT CONCAT('A', 'B') FROM MemberJ AS m";
        // Size (Team의 경우 members 컬렉션의 사이즈를 반환한다.)
        String sizeQuery = "SELECT SIZE(t.members) FROM TeamJ AS t";
        List<Integer> retuls = entityManager.createQuery(sizeQuery, Integer.class)
                .getResultList();

        System.out.println(retuls.toString());
    }

    private static void jpqlCondition(EntityManager entityManager) {
        String query = "SELECT COALESCE(m.username, '이름 없는 회원') FROM MemberJ AS m";
        String nullifQuery = "SELECT NULLIF(m.username, '관리자') FROM MemberJ AS m";
        List<String> result = entityManager.createQuery(nullifQuery, String.class)
                .getResultList();

        System.out.println("result = " + result.toString());
    }

    private static void jpqlEnum(EntityManager entityManager) {
        String query = "SELECT m.username, 'HELLO', TRUE FROM MemberJ AS m WHERE m.memberType = :memberType";
        List<Object[]> results = entityManager.createQuery(query)
                .setParameter("memberType", MemberType.USER)
                .getResultList();

        for (Object[] objects : results) {
            System.out.println("objects = " + objects[0]);
            System.out.println("objects = " + objects[1]);
            System.out.println("objects = " + objects[2]);
        }
    }

    private static void joinQuery(EntityManager entityManager) {
        String innerJoinQuery = "SELECT m FROM MemberJ AS m INNER JOIN m.teamJ AS t";
        String leftOuterJoinQuery = "SELECT m FROM MemberJ AS m LEFT OUTER JOIN m.teamJ AS t";
        String leftOuterJoinWithOnQuery = "SELECT m FROM MemberJ AS m LEFT OUTER JOIN m.teamJ AS t ON t.name = '개발팀'";
        String thetaJoin = "SELECT m FROM MemberJ AS m, TeamJ AS t WHERE m.username = t.name";
        String thetaJoinWithOnQuery = "SELECT m FROM MemberJ AS m LEFT OUTER JOIN TeamJ AS t ON m.username = t.name";
        List<MemberJ> results = entityManager.createQuery(thetaJoinWithOnQuery, MemberJ.class)
                .getResultList();
    }

    private static List<MemberJ> getPagingMember(EntityManager entityManager) {
        return entityManager.createQuery("SELECT m FROM MemberJ AS m ORDER BY m.age DESC", MemberJ.class)
                .setFirstResult(1)
                .setMaxResults(10)
                .getResultList();
    }

    private static void createDefaultJpqlAndQueryAPI(EntityManager entityManager) {
        // TypeQuery : 반환 타입이 명확한 경우
        // 메서드 체이닝으로 엮을 수 있다.
        MemberJ result = entityManager.createQuery("SELECT m FROM MemberJ AS m WHERE m.username = :username", MemberJ.class)
                .setParameter("username", "Hangyeol")
                .getSingleResult();

        System.out.println("singleResult = " + result.getUsername());

        TypedQuery<String> query2 = entityManager.createQuery(
                "SELECT m.username FROM MemberJ AS m", String.class
        );

        // Query : 반환 타입이 명확하지 않은 경우
        Query query3 = entityManager.createQuery(
                "SELECT m.username, m.age FROM MemberJ AS m"
        );
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
