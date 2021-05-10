package com.tommy.datajpa.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("SELECT m FROM Member m WHERE m.username = :username AND m.age = :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);
}
