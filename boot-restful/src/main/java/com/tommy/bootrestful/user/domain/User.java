package com.tommy.bootrestful.user.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * JsonIgnore 어노테이션을 사용하지 않고,
 * JsonIgnoreProperties 어노테이션으로 전역적인 필드 제어가 가능하다.
 */
@Getter
@NoArgsConstructor
//@JsonIgnoreProperties(value = {"password"})
@JsonFilter("UserInfo")
public class User {

    private Long id;

    @Size(min = 2, message = "Name은 2글자 이상 입력해주세요.")
    private String name;

    @Past
    private LocalDateTime joinedDate;

    private String password;

    private String ssn;

    public User(Long id, String name, LocalDateTime joinedDate, String password, String ssn) {
        this.id = id;
        this.name = name;
        this.joinedDate = joinedDate;
        this.password = password;
        this.ssn = ssn;
    }

    public void calculateUserId(long userId) {
        this.id = userId;
    }

    public void updateJoinedDate(LocalDateTime joinedDate) {
        this.joinedDate = joinedDate;
    }
}
