package com.tommy.querydsl.member.domain;

import com.tommy.querydsl.member.dto.MemberSearchCondition;
import com.tommy.querydsl.member.dto.MemberTeamDto;

import java.util.List;

public interface MemberCustomRepository {

    List<MemberTeamDto> search(MemberSearchCondition condition);

}
