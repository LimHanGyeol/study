package com.tommy.jpa.basic.study;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    // @OneToMany(mappedBy = "team") // members 는 Member.team으로 매핑이 되어 있다.
    // private List<Member> members = new ArrayList<>();

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    // public void subscription(Member member) {
       // this.members.add(member);
    // }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // public List<Member> getMembers() {
        // return members;
    // }
}
