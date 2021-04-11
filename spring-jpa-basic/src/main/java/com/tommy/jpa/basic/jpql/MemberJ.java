package com.tommy.jpa.basic.jpql;

import javax.persistence.*;

/**
 * JPQL 학습용 클래스이다.
 * 한 프로젝트에서 모든 예제를 관리하기 때문에 suffix 로 J를 붙였다.
 */
@Entity
public class MemberJ {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private TeamJ teamJ;

    public MemberJ() {
    }

    public MemberJ(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public TeamJ getTeam() {
        return teamJ;
    }
}
