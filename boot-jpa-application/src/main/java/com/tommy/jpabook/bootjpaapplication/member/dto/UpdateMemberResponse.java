package com.tommy.jpabook.bootjpaapplication.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberResponse {

    private Long id;
    private String name;

    public UpdateMemberResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
