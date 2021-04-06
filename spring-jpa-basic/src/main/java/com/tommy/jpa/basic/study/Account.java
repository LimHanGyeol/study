package com.tommy.jpa.basic.study;

import javax.persistence.*;
import java.time.LocalDate;

// @Table // 엔티티와 매핑할 테이블 지정
@Entity // JPA에서 사용할 엔티티 이름을 지정
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private LocalDate createdDate;

    private LocalDate lastModifiedDate;

    @Lob
    private String description;

    protected Account() {
    }

    public Account(Long id, String name, Integer age, RoleType roleType) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.roleType = roleType;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }
}
