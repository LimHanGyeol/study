package com.tommy.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tommy.querydsl.member.Member;
import com.tommy.querydsl.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.tommy.querydsl.member.QMember.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * WHERE 조건
 * member.username.eq("member1") -> =
 * member.username.nq("member1") -> !=
 * member.username.eq("member1").not() -> !=
 * <p>
 * member.username.isNotNull()
 * <p>
 * member.age.in() -> in(n, n)
 * member.age.notIn() -> not in(n, n)
 * member.age.between() -> between n, n
 * <p>
 * member.age.goe(30) -> age >= 30
 * member.age.gt(30) -> age > 30
 * member.age.loe(30) -> age <= 30
 * member.age.lt(30) -> age < 30
 * <p>
 * member.username.like("member%") -> like 검색
 * member.username.contains("member") -> like "%member%" 검색
 * member.username.startsWith("member") -> like "member%" 검색
 */
@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    private EntityManager entityManager;

    private Member member1;
    private Member member2;
    private Member member3;
    private Member member4;
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(entityManager);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        member1 = new Member("member1", 10);
        member1.participateTeam(teamA);
        entityManager.persist(member1);

        member2 = new Member("member2", 20);
        member2.participateTeam(teamA);
        entityManager.persist(member2);

        member3 = new Member("member3", 30);
        member3.participateTeam(teamB);
        entityManager.persist(member3);

        member4 = new Member("member4", 40);
        member4.participateTeam(teamB);
        entityManager.persist(member4);
    }

    @Test
    @DisplayName("JPQL을 이용하여 Member1 탐색")
    void startJPQL() {
        Member findMember = entityManager.createQuery(
                "SELECT m FROM Member m WHERE m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * QModel m1 = new QModel("m1"); 이런식으로 사용하면 JPQL에 할당되는 alias를 변경할 수 있다.
     * 같은 테이블을 조인하는 등의 사용을 할 수 있다.
     * 그 외에는 정적 팩토리 메서드와 static import 를 조합하여 사용하는 것을 권장한다.
     */
    @Test
    @DisplayName("QueryDSL을 이용하여 Member1 탐색")
    void startQueryDSL() {
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();// 하나를 가져온다.

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("다양한 검색조건(WHERE)을 사용할 수 있다.")
    void search() {
        Member findMember = queryFactory.selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("And 조건을 사용할 경우 ',' 로 대신할 수 있다..")
    void searchAnd() {
        Member findMember = queryFactory.selectFrom(member)
                .where(member.username.eq("member1"), (member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * fetch() : 리스트 조회, 없으면 EmptyList 리턴
     * fetchOne() : 단건 조회.
     * 결과가 없으면 null, 결과가 둘 이상이면 com.querydsl.core.NonUniqueResultException 발생
     * fetchFirst() : limit(1).fetchOne()
     * fetchResults() : 페이징 정보 포함, total count 쿼리 추가 실행
     * fetchCount() : count 쿼리로 변경해서 count 수 조회
     *
     * fetchResults의 경우 페이징 쿼리가 복잡해지면 Contents를 가져오는 쿼리와
     * 실제 totalCount를 가져오는 쿼리가 성능때문에 다를 경우가 있다.
     * 복잡하고 성능이 중요한 페이징 쿼리에서는 fetchResults를 쓰지말고 쿼리를 두번 날리는게 낫다.
     */
    @Test
    void resultFetch() {
        List<Member> fetch = queryFactory // 리스트 조회
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory // 단건 조회
                .selectFrom(member)
                .fetchOne();

        Member fetchFirst = queryFactory // limit 1 + 단건 조회. limit(1).fetchOne() 과 같다.
                .selectFrom(member)
                .fetchFirst();

        QueryResults<Member> fetchResults = queryFactory // 페이징
                .selectFrom(member)
                .fetchResults();

        long count = queryFactory
                .selectFrom(member)
                .fetchCount();
    }
}
