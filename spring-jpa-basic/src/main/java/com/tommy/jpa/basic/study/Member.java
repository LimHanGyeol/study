//package com.tommy.jpa.basic.study;
//
//import javax.persistence.*;
//
//@Entity
//public class Member {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MEMBER_ID")
//    private Long id;
//
//    @Column(name = "USERNAME")
//    private String name;
//
//    @ManyToOne
//    @JoinColumn(name = "TEAM_ID")
//    private Team team;
//
//    @OneToOne
//    @JoinColumn(name = "LOCKER_ID")
//    private Locker locker;
//
//    public Member() {
//    }
//
//    public Member(String name) {
//        this.name = name;
//    }
//
//    // 양방향 관계 설정 시에는 두 객체에 값을 설정해줘야 한다.
//    public void joinTeam(Team team) {
//        this.team = team;
//        team.subscription(this);
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Team getTeam() {
//        return team;
//    }
//}
