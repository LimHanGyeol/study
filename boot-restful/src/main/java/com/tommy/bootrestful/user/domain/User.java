package com.tommy.bootrestful.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class User {

    private Long id;

    @Size(min = 2, message = "Name은 2글자 이상 입력해주세요.")
    private String name;

    @Past
    private LocalDateTime joinedDate;

    public User(Long id, String name, LocalDateTime joinedDate) {
        this.id = id;
        this.name = name;
        this.joinedDate = joinedDate;
    }

    public void calculateUserId(long userId) {
        this.id = userId;
    }

    public void updateJoinedDate(LocalDateTime joinedDate) {
        this.joinedDate = joinedDate;
    }
}
