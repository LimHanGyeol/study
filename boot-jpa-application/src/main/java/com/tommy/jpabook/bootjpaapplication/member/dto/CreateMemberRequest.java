package com.tommy.jpabook.bootjpaapplication.member.dto;

import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class CreateMemberRequest {

    @NotEmpty
    private String name;

    public CreateMemberRequest(String name) {
        this.name = name;
    }

    public Member toMember() {
        return new Member(name);
    }
}
