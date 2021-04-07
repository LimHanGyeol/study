package com.tommy.jpa.basic.study;

import javax.persistence.*;

@Entity
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOCKER_ID")
    private Long id;

    private String name;

    // 양방향의 경우
    // @OneToOne(mappedBy = "locker")
    // private Member member;
}
