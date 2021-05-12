package com.tommy.datajpa.member.domain;

import com.tommy.datajpa.BaseEntity;
import com.tommy.datajpa.BaseJpaEntity;
import com.tommy.datajpa.team.domain.Team;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString(of = {"id", "username", "age"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void participateTeam(Team team) {
        this.team = team;
        team.allowMember(this);
    }

    public void updateName(String username) {
        this.username = username;
    }
}
