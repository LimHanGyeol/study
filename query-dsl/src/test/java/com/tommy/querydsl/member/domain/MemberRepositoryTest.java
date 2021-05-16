package com.tommy.querydsl.member.domain;

import com.tommy.querydsl.member.dto.MemberSearchCondition;
import com.tommy.querydsl.member.dto.MemberTeamDto;
import com.tommy.querydsl.team.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tommy.querydsl.member.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

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
    void basicTest() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow();

        assertThat(findMember).isEqualTo(member);

        List<Member> result = memberRepository.findAll();
        assertThat(result).containsExactly(member);

        List<Member> findUsernames = memberRepository.findByUsername("member1");
        assertThat(findUsernames).containsExactly(member);
    }

    @Test
    void searchTest() {
        MemberSearchCondition condition = new MemberSearchCondition("teamB", 35, 40);
        List<MemberTeamDto> result = memberRepository.search(condition);

        assertThat(result).extracting("username").containsExactly("member4");
    }

    @Test
    void searchPageSimpleAndComplex() {
        MemberSearchCondition condition = new MemberSearchCondition();
        PageRequest pageRequest = PageRequest.of(0, 3);

        // Page<MemberTeamDto> result = memberRepository.searchPageSimple(condition, pageRequest);
        Page<MemberTeamDto> result = memberRepository.searchPageComplex(condition, pageRequest);

        assertThat(result).hasSize(3);
        assertThat(result.getContent()).extracting("username")
                .containsExactly("member1", "member2", "member3");
    }

    @Test
    void queryDslPredicateExecutorTest() {
        Iterable<Member> result = memberRepository.findAll(
                member.age.between(10, 40)
                        .and(member.username.eq("member1"))
        );

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
}