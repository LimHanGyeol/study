package com.tommy.jpa.basic.study;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CASCADE 종류
 * ALL : 모두 적용 **
 * PERSIST : 영속 **
 * REMOVE : 삭제 **
 * MERGE : 병합
 * REFRESH : REFRESH
 * DETACH : DETACH
 *
 * 하나의 부모만 자식들을 관리할 때 CASCADE가 의미 있다.
 * 다른 테이블과도 연관관계가 맺어져 있으면 사용하면 안된다.
 *
 * orphanRemoval
 * 고아 객체 : 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
 * 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능이다.
 * 참조하는 곳이 하나일 때 사용해야 한다. ***
 * 특정 엔티티가 연관관계를 개인 소유할 때 사용한다.
 *
 * @OneToOne, @OneToMany만 가능하다.
 * 개념적으로 부모를 제거하면 자식은 고아가 된다.
 * 따라서 고아 객체 제거 기능을 활성화 하면, 부모를 제거할 때 자식도 함께 제거된다.
 * 이것은 CascadeType.REMOVE 처럼 동작한다.
 *
 * 영속성 전이 + 고아 객체, 생명주기
 * CascadeType.ALL + orphanRemoval=true
 * 스스로 생명주기를 관리하는 엔티티는 entityManager.persist()로 영속화, entityManager.remove()로 제거한다.
 *
 * 두 옵션을 모두 활성화 하면 부모 엔티티를 통해 자식 엔티티의 생명주기를 관리할 수 있다.
 * 도메인 주도 설계 (DDD)의 Aggregate Root 개념을 구현할 때 유용하다.
 */
@Entity
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.addParent(this);
    }

    public void removeChild(int index) {
        childList.remove(index);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Child> getChildList() {
        return Collections.unmodifiableList(childList);
    }
}
