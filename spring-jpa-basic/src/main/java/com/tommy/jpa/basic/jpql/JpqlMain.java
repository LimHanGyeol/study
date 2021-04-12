package com.tommy.jpa.basic.jpql;

import com.tommy.jpa.basic.jpashop.domain.Member;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.tommy.jpa.basic.jpql.Projection.*;

public class JpqlMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        try {
            // Fetch Join
            // fetchJoin(entityManager);

            // 엔티티 직접 사용
            // entity(entityManager, member1);


            TeamJ teamA = new TeamJ("개발팀");
            entityManager.persist(teamA);

            TeamJ teamB = new TeamJ("운영팀");
            entityManager.persist(teamB);

            MemberJ member1 = new MemberJ("회원1", 27);
            member1.joinTeam(teamA);
            entityManager.persist(member1);

            MemberJ member2 = new MemberJ("회원2", 28);
            member2.joinTeam(teamA);
            entityManager.persist(member2);

            MemberJ member3 = new MemberJ("회원3", 28);
            member3.joinTeam(teamB);
            entityManager.persist(member3);

            entityManager.flush();
            entityManager.clear();


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }

    private static void entity(EntityManager entityManager, MemberJ member1) {
        // 엔티티 직접 사용
        // 엔티티로 조회할 경우 엔티티의 기본 키로 SELECT SQL 이 나간다.
        // String query = "SELECT m FROM MemberJ AS m WHERE m = :member";
        String query = "SELECT m FROM MemberJ AS m WHERE m.id = :memberId";

        // 외래키 직접 사용은 외래키 FK인 TEAM_ID와 매핑 된다.
        String fkQuery = "SELECT m FROM MemberJ AS m WHERE m.teamJ = :team";
        MemberJ findMember = entityManager.createQuery(query, MemberJ.class)
                .setParameter("memberId", member1.getId())
                .getSingleResult();
//                    .setParameter("member", member1)

        System.out.println("findMember = " + findMember);
    }

    private static void fetchJoin(EntityManager entityManager) {
        // ManyToOne 관계 Fetch Join
        String query = "SELECT m FROM MemberJ AS m JOIN FETCH m.teamJ";

        // Collection Fetch Join. 일대다 관계
        // 컬렉션 페치 조인을 하면 일대다 조인이기 때문에 모든 데이터를 엘리먼트 만큼 가지고 온다.
        //  WHERE t.name = '개발팀'
        String collectionFetchJoinQuery = "SELECT DISTINCT t FROM TeamJ AS t JOIN FETCH t.members";
        List<TeamJ> results = entityManager.createQuery(collectionFetchJoinQuery, TeamJ.class)
                .getResultList();

        for (TeamJ result : results) {
            System.out.println("result = " + result + ", " + result.getMembers().size());
            for (MemberJ member : result.getMembers()) {
                System.out.println(" -> member = " + member);
            }
        }
    }
}
