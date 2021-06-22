package com.tommy.bootrestful.user.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;

@Getter
@JsonFilter("UserInfoV2")
public class UserV2 extends User {

    private String grade;

    public UserV2(User user, String grade) {
        super(user.getId(), user.getName(), user.getJoinedDate(), user.getPassword(), user.getSsn());
        this.grade = grade;
    }
}
