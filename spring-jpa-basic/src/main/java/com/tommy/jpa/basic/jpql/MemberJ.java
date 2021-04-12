package com.tommy.jpa.basic.jpql;

import javax.persistence.*;

/**
 * JPQL 학습용 클래스이다.
 * 한 프로젝트에서 모든 예제를 관리하기 때문에 suffix 로 J를 붙였다.
 *
 * 네임드 쿼리는 애플리케이션 로딩 시점에 JPA가 파싱할 수 있다.
 * 문법에 맞지 않은 쿼리일 경우 오류가 생긴다.
 * 재사용 및 애플리케이션 로딩 시점에 오류를 잡을수 있어 강력하다.
 */
@Entity
@NamedQuery(
        name = "MemberJ.findByUsername",
        query = "SELECT m FROM MemberJ as m WHERE m.username = :username"
)
public class MemberJ {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private TeamJ teamJ;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    public MemberJ() {
    }

    public MemberJ(String username, int age) {
        this.username = username;
        this.age = age;
        this.memberType = MemberType.USER;
    }

    public void joinTeam(TeamJ team) {
        this.teamJ = team;
        team.subscription(this);
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

    @Override
    public String toString() {
        return "MemberJ{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
