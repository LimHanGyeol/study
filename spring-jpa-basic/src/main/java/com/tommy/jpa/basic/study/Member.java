package com.tommy.jpa.basic.study;

import javax.persistence.*;

/**
 * 대부분의 JPA 구현체들은 fetch.EAGER로 조회할 경우
 * 조인 쿼리로 가져온다.
 *
 * 가급적 실무에서는 지연 로딩만 사용하자.
 * 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생한다.
 * 즉시 로딩은 JPQL에서 N + 1문제를 일으킨다.
 * @ManyToOne, @OneToOne은 기본이 EAGER. 즉시 로딩이다. 이를 LAZY로 설정하자.
 * @OneToMany, @ManyToMany는 기본이 지연로딩이다.
 */
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) // EAGER 즉시 로딩. 프록시가 아닌 실제 엔티티를 가져온다.
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    public Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    // 양방향 관계 설정 시에는 두 객체에 값을 설정해줘야 한다.
    public void joinTeam(Team team) {
        this.team = team;
        team.subscription(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }
}
