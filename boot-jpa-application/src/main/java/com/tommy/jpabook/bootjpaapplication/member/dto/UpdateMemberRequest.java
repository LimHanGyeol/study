package com.tommy.jpabook.bootjpaapplication.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberRequest {

    private String name;

    public UpdateMemberRequest(String name) {
        this.name = name;
    }
}
