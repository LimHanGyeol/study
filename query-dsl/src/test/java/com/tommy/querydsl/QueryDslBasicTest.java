package com.tommy.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tommy.querydsl.member.Member;
import com.tommy.querydsl.member.QMember;
import com.tommy.querydsl.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static com.tommy.querydsl.member.QMember.*;
import static com.tommy.querydsl.team.QTeam.*;
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
     * <p>
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

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순 (DESC)
     * 2. 회원 이름 올림차순 (ASC)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    void sort() {
        entityManager.persist(new Member(null, 100));
        entityManager.persist(new Member("member5", 100));
        entityManager.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    void paging1() {
        List<Member> results = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(0)
                .limit(2)
                .fetch();

        assertThat(results).hasSize(2);
    }

    /**
     * 실무에서는 fetchResults를 쓰지 못할 경우가 있다.
     * Contents와 Count 쿼리를 따로 작성해야 하는 경우가 있다.
     */
    @Test
    @DisplayName("전체 조회수가 필요한 경우")
    void paging2() {
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults()).hasSize(2);
    }

    @Test
    @DisplayName("집계")
    void aggregation() {
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min())
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라.
     */
    @Test
    @DisplayName("그룹핑")
    void group() {
        List<Tuple> results = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name) // .having()
                .fetch();

        Tuple teamA = results.get(0);
        Tuple teamB = results.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /**
     * 팀 A에 소속된 모든 회원
     */
    @Test
    void join() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team) // innerJoin(), left(right)Join() 도 사용할 수 있다.
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result).extracting("username")
                .containsExactly("member1", "member2");
    }

    /**
     * 세타 조인
     * 회원의 이름이 팀 이름과 같은 회원 조회
     * Cross Join으로 곱집합으로 연산된다.
     */
    @Test
    void theta_join() {
        entityManager.persist(new Member("teamA", 10));
        entityManager.persist(new Member("teamB", 10));
        entityManager.persist(new Member("teamC", 10));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL : SELECT m, t FROM Member m LEFT JOIN m.team t ON t.name = 'teamA'
     * 결과를 출력할 때 JPQL에서 with 문법을 사용한다.
     */
    @Test
    void join_on_filtering() {
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     * 크로스 조인을 할 경우
     * leftJoin() 파라미터에 일반 조인과 다르게 엔티티 하나만 들어간다.
     * Ex) from(member).leftJoin(team).on(xxx)
     */
    @Test
    @DisplayName("연관관계가 없는 엔티티를 외부조인 할 때 on절 사용")
    void join_on_no_relation() {
        entityManager.persist(new Member("teamA", 10));
        entityManager.persist(new Member("teamB", 10));
        entityManager.persist(new Member("teamC", 10));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Test
    void non_fetch_join() {
        entityManager.flush();
        entityManager.clear();

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        boolean isLoaded = entityManagerFactory.getPersistenceUnitUtil()
                .isLoaded(findMember.getTeam());
        assertThat(isLoaded).as("fetch join 미적용").isFalse();
    }

    @Test
    void fetch_join() {
        entityManager.flush();
        entityManager.clear();

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .join(member.team, team)
                .fetchJoin()
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        boolean isLoaded = entityManagerFactory.getPersistenceUnitUtil()
                .isLoaded(findMember.getTeam());
        assertThat(isLoaded).as("fetch join 미적용").isTrue();
    }

    /**
     * 나이가 가장 많은 회원 조회
     */
    @Test
    void subQuery() {
        QMember subMember = new QMember("subMember");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(subMember.age.max())
                                .from(subMember)
                )).fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    /**
     * 나이가 평균 이상인 회원 조회
     */
    @Test
    void subQueryGoe() {
        QMember subMember = new QMember("subMember");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(subMember.age.avg())
                                .from(subMember)
                )).fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    /**
     * 나이가 평균 이상인 회원 조회
     * 효율적인 쿼리는 아니다.
     */
    @Test
    void subQueryGoeIn() {
        QMember subMember = new QMember("subMember");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(subMember.age)
                                .from(subMember)
                                .where(subMember.age.gt(10))
                )).fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    @Test
    void selectSubQuery() {
        QMember subMember = new QMember("subMember");

        List<Tuple> result = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(subMember.age.avg())
                                .from(subMember))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    void basicCase() {
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String print : result) {
            System.out.println("print = " + print);
        }
    }

    @Test
    void complexCase() {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String print : result) {
            System.out.println("print = " + print);
        }
    }

    @Test
    void constant() {
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * stringValue를 이용하여 형변환 할 수 있다.
     */
    @Test
    void concat() {
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String print : result) {
            System.out.println("print = " + print);
        }
    }
}
