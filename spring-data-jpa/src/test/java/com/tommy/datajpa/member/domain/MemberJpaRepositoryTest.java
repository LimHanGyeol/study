package com.tommy.datajpa.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
        // Rollback(false)로 데이터 쿼리가 나가는지 확인할 수 있다.
    void testMember() {
        // given
        Member member = new Member("tommy", 27);

        // when
        Member savedMember = memberJpaRepository.save(member);

        // then
        Member findMember = memberJpaRepository.findById(savedMember.getId());
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void updateName() {
        // given
        Member member = new Member("tommy", 27);
        Member savedMember = memberJpaRepository.save(member);

        // when
        savedMember.updateName("hangyeol");

        // then
        assertThat(savedMember.getUsername()).isEqualTo("hangyeol");
    }

    @Test
    @DisplayName("Member, Team Domain 통합 테스트")
    void basicCRUD() {
        Member member1 = new Member("member1", 27);
        Member member2 = new Member("member2", 28);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberJpaRepository.findBy(member1.getId()).orElseThrow();
        Member findMember2 = memberJpaRepository.findBy(member2.getId()).orElseThrow();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members).hasSize(2);

        // 개수 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        // 개수 검증
        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        // given
        Member member1 = new Member("tommy", 10);
        Member member2 = new Member("tommy", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when
        List<Member> findMembers = memberJpaRepository.findByUsernameAndAgeGreaterThen("tommy", 15);

        // then
        assertThat(findMembers).hasSize(1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo("tommy");
        assertThat(findMembers.get(0).getAge()).isEqualTo(20);
    }
}
