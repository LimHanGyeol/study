package com.tommy.querydsl.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
public class MemberDto {

    private String username;
    private int age;

    @QueryProjection
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
