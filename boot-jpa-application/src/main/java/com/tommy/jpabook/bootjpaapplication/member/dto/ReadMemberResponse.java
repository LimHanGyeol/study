package com.tommy.jpabook.bootjpaapplication.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadMemberResponse {

    private String name;

    public ReadMemberResponse(String name) {
        this.name = name;
    }
}
