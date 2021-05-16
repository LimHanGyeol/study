package com.tommy.querydsl.member.controller;

import com.tommy.querydsl.member.domain.MemberJpaRepository;
import com.tommy.querydsl.member.dto.MemberSearchCondition;
import com.tommy.querydsl.member.dto.MemberTeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberJpaRepository memberJpaRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.search(condition);
    }
}
