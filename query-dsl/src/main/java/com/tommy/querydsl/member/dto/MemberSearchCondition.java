package com.tommy.querydsl.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원명, 팀명, 나이(ageGeo, ageLoe)
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberSearchCondition {

    private String username;
    private String teamName;
    private Integer ageGoe;
    private Integer ageLoe;

}
