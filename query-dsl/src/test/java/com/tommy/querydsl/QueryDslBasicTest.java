package com.tommy.querydsl;

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

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @DisplayName("QueryDSL을 이용하여 Member1 탐색")
    void startQueryDSL() {
        QMember member = new QMember("m"); // 어떤 QMember 인지 구분할 수 있게 해주는 이름

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();// 하나를 가져온다.

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
