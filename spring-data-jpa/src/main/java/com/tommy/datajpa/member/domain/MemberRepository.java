package com.tommy.datajpa.member.domain;

import com.tommy.datajpa.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("SELECT m FROM Member m WHERE m.username = :username AND m.age = :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);

    @Query("SELECT m.username FROM Member m")
    List<String> findUsernames();

    @Query("SELECT NEW com.tommy.datajpa.member.dto.MemberDto(m.id, m.username, t.name) FROM Member m JOIN m.team t")
    List<MemberDto> findMemberDto();

    @Query("SELECT m FROM Member m WHERE m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);

    Member findMemberByUsername(String username);

    Optional<Member> findOptionalByUsername(String username);

    // 페이징된 데이터만 가져오고 싶을 경우 page 타입을 List로 가져올 수 있다.
    @Query(value = "SELECT m FROM Member m LEFT JOIN m.team t",
            countQuery = "SELECT COUNT(m.username) FROM Member m")
    Slice<Member> findByAge(int age, Pageable pageable);

    @Modifying // 벌크 업데이트시 해당 어노테이션이 필수이다. JPA에서 executeUpdate의 역할을 한다.
    @Query("UPDATE Member m SET m.age = m.age + 1 WHERE m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("SELECT m FROM Member m INNER JOIN FETCH m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) // 내부적으로 패치 조인이 된다. JPQL 을 사용하지 않고도 사용할 수 있다.
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("SELECT m FROM Member m") // 이런식으로 JPQL 을 사용해도 fetch join을 할 수 있다.
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = "team")
    List<Member> findByUsername(@Param("username") String username);
}
