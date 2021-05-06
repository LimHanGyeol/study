package com.tommy.jpabook.bootjpaapplication.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMemberResponse {

    private Long id;

    public CreateMemberResponse(Long id) {
        this.id = id;
    }
}
