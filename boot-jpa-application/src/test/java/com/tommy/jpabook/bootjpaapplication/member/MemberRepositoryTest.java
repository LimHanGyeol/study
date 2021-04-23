package com.tommy.jpabook.bootjpaapplication.member;

import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import com.tommy.jpabook.bootjpaapplication.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Member 생성 테스트")
    @Rollback(value = false)
    void testMember() {
        // given
        Member member = new Member("hangyeol");

        // when
        Long savedMemberId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedMemberId);

        // then
        assertThat(findMember.getId()).isEqualTo(savedMemberId);
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember).isEqualTo(member);
    }
}
