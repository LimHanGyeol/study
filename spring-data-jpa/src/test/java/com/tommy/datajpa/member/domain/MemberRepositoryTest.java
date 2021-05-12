package com.tommy.datajpa.member.domain;

import com.tommy.datajpa.member.dto.MemberDto;
import com.tommy.datajpa.team.domain.Team;
import com.tommy.datajpa.team.domain.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    private static final Member MEMBER_HANGYEOL = new Member("hangyeol", 27);
    private static final Member MEMBER_TOMMY = new Member("tommy", 10);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityManager entityManager;

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

    @Test
    void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        //Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Slice<Member> pageSlice = memberRepository.findByAge(age, pageRequest);

        // then
        assertThat(pageSlice.getContent()).hasSize(3); // 페이징 된 데이터
//        assertThat(pageSlice.getTotalElements()).isEqualTo(5); // totalCount (5) Slice는 전체 Count를 가져오지 않음.
        assertThat(pageSlice.getNumber()).isEqualTo(0); // 페이지 번호
//        assertThat(pageSlice.getTotalPages()).isEqualTo(2); // 전체 페이지 개수 Slice는 전체 페이지 개수를 가져오지 않는다.
        assertThat(pageSlice.isFirst()).isTrue(); // 첫 번째 페이지인지
        assertThat(pageSlice.hasNext()).isTrue(); // 다음 페이지가 존재 하는지
    }

    @Test
    void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    @DisplayName("엔티티그래프와 Fetch Join")
    void findMemberLazy() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10);
        member1.participateTeam(teamA);
        memberRepository.save(member1);

        Member member2 = new Member("member2", 10);
        member2.participateTeam(teamB);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();

        // when
        List<Member> members = memberRepository.findByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    /**
     * 쿼리를 읽기전용으로 가져오고 싶을 경우 QueryHints를 사용하면 더티체킹의 객체 스냅샷을 체크하지 않는다.
     * 하지만 성능저하의 원인은 읽기전용 보다는 잘못된 쿼리가 나가는 경우가 크므로
     * 성능 최적화를 하기 위해 적용하기에는 큰 효율성은 없다.
     * 감으로 사용하지 말고 성능 테스트를 해본 후 고려하자.
     * 그리고 성능이 좋지 않다고 판단될 경우에는 이미 캐시를 이용한 레디스 적용을 고려하고 있을 것이다.
     * 그래서 쿼리힌트로 얻을 수 있는 이점은 크지 않다.
     * 처음부터 튜닝을 하는 접근 방법이 좋다고 생각 들진 않는다.
     */
    @Test
    void queryHint() {
        // given
        Member savedMember = memberRepository.save(MEMBER_TOMMY);
        entityManager.flush();
        entityManager.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("tommy");
        findMember.updateName("kkkk");
        entityManager.flush();
    }

    @Test
    void lock() {
        // given
        Member savedMember = memberRepository.save(MEMBER_TOMMY);
        entityManager.flush();
        entityManager.clear();

        // when
        List<Member> findMember = memberRepository.findLockByUsername("tommy");
    }
}
