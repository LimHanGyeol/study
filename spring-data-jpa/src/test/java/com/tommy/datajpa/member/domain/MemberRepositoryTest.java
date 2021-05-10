package com.tommy.datajpa.member.domain;

import com.tommy.datajpa.member.dto.MemberDto;
import com.tommy.datajpa.team.domain.Team;
import com.tommy.datajpa.team.domain.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    private static final Member MEMBER_HANGYEOL = new Member("hangyeol", 27);
    private static final Member MEMBER_TOMMY = new Member("tommy", 10);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void testMember() {
        // when
        Member savedMember = memberRepository.save(MEMBER_HANGYEOL);

        // then
        Member findMember = memberRepository.findById(savedMember.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(findMember.getUsername()).isEqualTo(MEMBER_HANGYEOL.getUsername());
    }

    @Test
    @DisplayName("Member, Team Domain 통합 테스트")
    void basicCRUD() {
        memberRepository.save(MEMBER_HANGYEOL);
        memberRepository.save(MEMBER_TOMMY);

        // 단건 조회
        Member findMember1 = memberRepository.findById(MEMBER_HANGYEOL.getId()).orElseThrow();
        Member findMember2 = memberRepository.findById(MEMBER_TOMMY.getId()).orElseThrow();
        assertThat(findMember1).isEqualTo(MEMBER_HANGYEOL);
        assertThat(findMember2).isEqualTo(MEMBER_TOMMY);

        // 리스트 조회
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(2);

        // 개수 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제
        memberRepository.delete(MEMBER_HANGYEOL);
        memberRepository.delete(MEMBER_TOMMY);

        // 개수 검증
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        // given
        memberRepository.save(MEMBER_HANGYEOL);
        memberRepository.save(MEMBER_TOMMY);

        // when
        List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("hangyeol", 15);

        // then
        assertThat(findMembers).hasSize(1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo("hangyeol");
        assertThat(findMembers.get(0).getAge()).isEqualTo(27);
    }

    @Test
    void findMember() {
        // given
        Member savedMember = memberRepository.save(MEMBER_HANGYEOL);

        // when
        List<Member> findMembers = memberRepository.findMember("hangyeol", 27);

        // then
        assertThat(findMembers.get(0)).isEqualTo(savedMember);
    }

    @Test
    void findUsernames() {
        // given
        memberRepository.save(MEMBER_HANGYEOL);
        memberRepository.save(MEMBER_TOMMY);

        // when
        List<String> usernames = memberRepository.findUsernames();

        // then
        assertThat(usernames).containsExactly("hangyeol", "tommy");
    }

    @Test
    void findMemberDto() {
        // given
        Team teamBlue = new Team("teamBlue");
        teamRepository.save(teamBlue);

        MEMBER_HANGYEOL.participateTeam(teamBlue);
        memberRepository.save(MEMBER_HANGYEOL);

        // when
        List<MemberDto> memberDto = memberRepository.findMemberDto();

        // then
        assertThat(memberDto).hasSize(1);
    }

    @Test
    void findByNames() {
        // given
        memberRepository.save(MEMBER_HANGYEOL);

        memberRepository.save(MEMBER_TOMMY);

        // when
        List<Member> result = memberRepository.findByNames(List.of("hangyeol", "tommy"));

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void returnType() {
        // given
        memberRepository.save(MEMBER_HANGYEOL);
        memberRepository.save(MEMBER_TOMMY);

        // when
        // Collection의 경우 유효하지 않은 파라미터를 전달하면 EmptyCollection을 반환한다. 방어코드를 쓸 필요가 없다.
        List<Member> members = memberRepository.findListByUsername("tommy");
        // 단건 조회의 경우 DB에서 조회할 때 데이터가 있을 수도 있고, 없을 수도 있는 상황이라면 Optional을 쓰자.
        // 단건 조회를 하는데 결과가 n개일 경우 Exception이 발생한다.
        // JPA의 Exception을 Spring이 감싸 IncorrectResultSizeDataAccessException 등.. 추상화된 예외가 발생한다.
        Member findMember = memberRepository.findMemberByUsername("hangyeol");
        Member optionalMember = memberRepository.findOptionalByUsername("tommy").orElseThrow();
    }
}
