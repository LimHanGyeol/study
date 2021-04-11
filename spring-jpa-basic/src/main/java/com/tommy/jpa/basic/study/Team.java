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

     @OneToMany(mappedBy = "team") // members 는 Member.team으로 매핑이 되어 있다.
     private List<Member1> member1s = new ArrayList<>();

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

     public void subscription(Member1 member1) {
        this.member1s.add(member1);
     }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

     public List<Member1> getMembers() {
         return member1s;
     }
}
