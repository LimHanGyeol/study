package com.tommy.datajpa.member.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class MemberCustomRepositoryImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void customCall() {
        List<Member> members = memberRepository.findMemberCustom();
    }
}
