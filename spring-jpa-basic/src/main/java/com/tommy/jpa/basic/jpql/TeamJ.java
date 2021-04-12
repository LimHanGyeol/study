package com.tommy.jpa.basic.jpql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * JPQL 학습용 클래스이다.
 * 한 프로젝트에서 모든 예제를 관리하기 때문에 suffix 로 J를 붙였다.
 */
@Entity
public class TeamJ {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "teamJ")
    private List<MemberJ> members = new ArrayList<>();

    public TeamJ() {
    }

    public TeamJ(String name) {
        this.name = name;
    }

    public void subscription(MemberJ member) {
        this.members.add(member);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MemberJ> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "TeamJ{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
