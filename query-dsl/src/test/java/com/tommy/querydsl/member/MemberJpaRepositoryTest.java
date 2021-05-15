package com.tommy.querydsl.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    void basicTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId())
                .orElseThrow();

        assertThat(findMember).isEqualTo(member);

        List<Member> result = memberJpaRepository.findAll_QueryDSL();
        assertThat(result).containsExactly(member);

        List<Member> findUsernames = memberJpaRepository.findByUsername_QueryDSL("member1");
        assertThat(findUsernames).containsExactly(member);
    }
}