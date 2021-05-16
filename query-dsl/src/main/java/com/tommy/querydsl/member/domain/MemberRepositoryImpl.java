package com.tommy.querydsl.member.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tommy.querydsl.member.QMemberTeamDto;
import com.tommy.querydsl.member.dto.MemberSearchCondition;
import com.tommy.querydsl.member.dto.MemberTeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.tommy.querydsl.member.QMember.member;
import static com.tommy.querydsl.team.QTeam.team;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return member.username.eq(username);
    }

    private BooleanExpression teamNameEq(String teamName) {
        if (!StringUtils.hasText(teamName)) {
            return null;
        }
        return team.name.eq(teamName);
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        if (ageGoe == null) {
            return null;
        }
        return member.age.goe(ageGoe);
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        if (ageLoe == null) {
            return null;
        }
        return member.age.loe(ageLoe);
    }
}
