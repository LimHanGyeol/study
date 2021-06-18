package com.tommy.bootrestful.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private LocalDateTime joinedDate;

    public User(Long id, String name, LocalDateTime joinedDate) {
        this.id = id;
        this.name = name;
        this.joinedDate = joinedDate;
    }

    public void calculateUserId(long userId) {
        this.id = userId;
    }
}
