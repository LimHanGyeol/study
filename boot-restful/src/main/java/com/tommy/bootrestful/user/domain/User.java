package com.tommy.bootrestful.user.domain;

import com.tommy.bootrestful.post.domain.Post;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonIgnore 어노테이션을 사용하지 않고,
 * JsonIgnoreProperties 어노테이션으로 전역적인 필드 제어가 가능하다.
 */
//@JsonIgnoreProperties(value = {"password"})
//@JsonFilter("UserInfo") // HATEOAS 테스트 시 주석
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "Name은 2글자 이상 입력해주세요.")
    @ApiModelProperty(notes = "사용자 이름을 입력해주세요.")
    private String name;

    @Past
    @ApiModelProperty(notes = "사용자 등록일을 입력해주세요.")
    private LocalDateTime joinedDate;

    @ApiModelProperty(notes = "사용자 패스워드를 입력해주세요.")
    private String password;

    @ApiModelProperty(notes = "사용자 주민번호를 입력해주세요.")
    private String ssn;

    @OneToMany(mappedBy = "user")
    private final List<Post> posts = new ArrayList<>();

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

    public void writePost(Post post) {
        this.posts.add(post);
    }
}
