package com.tommy.jpa.basic.study.shop;

import javax.persistence.*;

/**
 * 상속 관계로 JPA 테이블을 설계하게 될 경우, JOINED 전략이 기본으로 권장된다.
 *
 * DiscriminatorColumn 으로 DTYPE 을 설정할 수 있다.
 * 해당 어노테이션의 name 속성으로 칼럼명을 바꿔줄 수 있지만, 관례로 인해 DTYPE 을 권장한다.
 *
 * SingleTable 전략으로 사용할 경우 InheritanceType.SINGLE_TABLE 로 변경하면 된다.
 * InheritanceType.TABLE_PER_CLASS은 권장되지 않는다.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // 단일 테이블 전략시 없어도 DTYPE Column이 생성된다.
public abstract class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;

    public Items() {
    }

    public Items(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
