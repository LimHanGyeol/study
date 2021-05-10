package com.tommy.datajpa.member.domain;

import com.tommy.datajpa.member.dto.MemberDto;
import com.tommy.datajpa.team.domain.Team;
import com.tommy.datajpa.team.domain.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void testMember() {
        // given
        Member member = new Member("hangyeol", 27);

        // when
        Member savedMember = memberRepository.save(member);

        // then
        Member findMember = memberRepository.findById(savedMember.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("Member, Team Domain 통합 테스트")
    void basicCRUD() {
        Member member1 = new Member("member1", 27);
        Member member2 = new Member("member2", 28);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).orElseThrow();
        Member findMember2 = memberRepository.findById(member2.getId()).orElseThrow();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(2);

        // 개수 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        // 개수 검증
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        // given
        Member member1 = new Member("tommy", 10);
        Member member2 = new Member("tommy", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("tommy", 15);

        // then
        assertThat(findMembers).hasSize(1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo("tommy");
        assertThat(findMembers.get(0).getAge()).isEqualTo(20);
    }

    @Test
    void findMember() {
        // given
        Member member = new Member("tommy", 27);
        Member savedMember = memberRepository.save(member);

        // when
        List<Member> findMembers = memberRepository.findMember("tommy", 27);

        // then
        assertThat(findMembers.get(0)).isEqualTo(savedMember);
    }

    @Test
    void findUsernames() {
        // given
        Member member1 = new Member("tommy", 27);
        memberRepository.save(member1);

        Member member2 = new Member("hangyeol", 27);
        memberRepository.save(member2);

        // when
        List<String> usernames = memberRepository.findUsernames();

        // then
        assertThat(usernames).containsExactly("tommy", "hangyeol");
    }

    @Test
    void findMemberDto() {
        // given
        Team teamBlue = new Team("teamBlue");
        teamRepository.save(teamBlue);

        Member member = new Member("tommy", 27);
        member.participateTeam(teamBlue);
        memberRepository.save(member);

        // when
        List<MemberDto> memberDto = memberRepository.findMemberDto();

        // then
        assertThat(memberDto).hasSize(1);
    }

    @Test
    void findByNames() {
        // given
        Member member1 = new Member("tommy", 27);
        memberRepository.save(member1);

        Member member2 = new Member("hangyeol", 27);
        memberRepository.save(member2);

        // when
        List<Member> result = memberRepository.findByNames(List.of("tommy", "hangyeol"));

        // then
        assertThat(result).hasSize(2);
    }
}
