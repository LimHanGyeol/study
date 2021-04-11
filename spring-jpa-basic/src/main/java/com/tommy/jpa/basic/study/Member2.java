package com.tommy.jpa.basic.study;

import com.tommy.jpa.basic.jpashop.domain.Address;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 값 타입 컬렉션을 학습하기 위한 객체
 * <p>
 * 값 타입 컬렉션의 라이프사이클은 상위 엔티티의 라이프 사이클을 따른다.
 * 즉, Cascade.ALL + 고아 객체 제거 기능을 선언한 상태와 같다.
 * 값 타입 컬렉션이 적용되는 @ElementCollection 또한 fetchMode.LAZY로 설정되어 지연 로딩 된다.
 * <p>
 * 값 타입은 불변이여야 한다. 가변 객체로 설정을 하면 참조 공유 문제가 생길 수 있다.
 * 값 타입 및 값 컬렉션을 수정할 경우에는 새로운 객체를 생성하여 갈아끼워 넣어줘야 한다.
 */
@Entity
public class Member2 {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER2_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD",
            joinColumns = @JoinColumn(name = "MEMBER_ID") // 외래키 설정
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection
//    @CollectionTable(name = "ADDRESS",
//            joinColumns = @JoinColumn(name = "MEMBER_ID") // 외래키 설정
//    )
//    private List<Address> addressHistory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();

    public Member2() {
    }

    public Member2(String username, Address homeAddress) {
        this.username = username;
        this.homeAddress = homeAddress;
    }

    public void addFavoriteFood(String food) {
        favoriteFoods.add(food);
    }

    public void addAddressHistory(AddressEntity address) {
        addressHistory.add(address);
    }

    public void updateHomeAddress(Address address) {
        this.homeAddress = address;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }
}
