package com.tommy.querydsl.member;

import com.tommy.querydsl.member.domain.Member;
import com.tommy.querydsl.team.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    private EntityManager entityManager;

    private Member member1;
    private Member member2;
    private Member member3;
    private Member member4;

    @BeforeEach
    void setUp() {
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
    void testEntity() {
        entityManager.flush();
        entityManager.clear();

        List<Member> members = entityManager.createQuery(
                "SELECT m FROM Member m", Member.class)
                .getResultList();

        assertThat(members).hasSize(4);
    }
}