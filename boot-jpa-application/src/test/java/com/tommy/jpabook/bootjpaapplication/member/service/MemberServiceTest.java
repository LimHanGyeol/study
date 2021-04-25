package com.tommy.jpabook.bootjpaapplication.member.service;

import com.tommy.jpabook.bootjpaapplication.member.domain.Address;
import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import com.tommy.jpabook.bootjpaapplication.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Profile("test")
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("서울", "테헤란로", "12338");
    }

    @Test
    @DisplayName("회원 가입")
    void register() {
        // given
        Member member = new Member("hangyeol", address);

        // when
        Long savedMemberId = memberService.register(member);

        // then
        assertThat(savedMemberId).isEqualTo(member.getId());
        assertThat(member).isEqualTo(memberRepository.findById(savedMemberId));
    }

    @Test
    @DisplayName("회원 이름이 존재할 경우 예외 발생")
    void validateDuplicateMember() {
        // given
        Member member1 = new Member("tommy", address);
        Member member2 = new Member("tommy", address);

        memberService.register(member1);

        // when & then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> memberService.register(member2));
    }
}
