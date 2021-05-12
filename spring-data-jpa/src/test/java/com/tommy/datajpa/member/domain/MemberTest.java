package com.tommy.datajpa.member.domain;

import com.tommy.datajpa.team.domain.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testEntity() {
        // given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Team teamB = new Team("teamB");
        entityManager.persist(teamB);

        Member member1 = new Member("member1", 27);
        member1.participateTeam(teamA);
        Member member2 = new Member("member2", 27);
        member2.participateTeam(teamA);
        Member member3 = new Member("member3", 28);
        member3.participateTeam(teamB);
        Member member4 = new Member("member4", 28);
        member4.participateTeam(teamB);

        // when
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);

        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();

        // 확인
        List<Member> members = entityManager.createQuery(
                "SELECT m FROM Member m", Member.class
        ).getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }

    @Test
    void jpaEventBaseEntity() throws InterruptedException {
        // given
        Member member = new Member("member1", 10);
        memberRepository.save(member); // @PrePersist

        Thread.sleep(100);
        member.updateName("update");

        entityManager.flush();
        entityManager.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).orElseThrow();

        // then
        System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getUpdatedDate() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
    }
}
