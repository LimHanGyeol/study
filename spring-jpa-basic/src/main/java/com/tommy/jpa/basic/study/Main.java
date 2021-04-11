package com.tommy.jpa.basic.study;

import com.tommy.jpa.basic.jpashop.domain.Address;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        try {
            Address address = new Address("homeCity", "street", "12069");
            Member2 member = new Member2("hangyeol", address);

            member.addFavoriteFood("치킨");
            member.addFavoriteFood("피자");
            member.addFavoriteFood("국밥");

            member.addAddressHistory(new AddressEntity("old1", "oldStreet1", "1234"));
            member.addAddressHistory(new AddressEntity("old2", "oldStreet2", "1235"));

            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

            System.out.println("==============");
            Member2 findMember = entityManager.find(Member2.class, member.getId());

            // findMemberInfo(findMember);

            // Member2 updatedMember = updateHomeAddress(findMember);

            // 값 타입 컬렉션 업데이트, 치킨 -> 한식
            // 컬렉션의 값만 변경해도 뭐가 변경되어야 하는지 JPA가 알고 바꿔준다.
//            updateFavoriteFood(findMember);

            // AddressHistory 삭제
            System.out.println("=========== UPDATE");
//            findMember.getAddressHistory().remove(new AddressEntity("old1", "oldStreet1", "1234"));
//            findMember.getAddressHistory().add(new AddressEntity("newCity1", "oldStreet1", "1234"));

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }

    private static void updateFavoriteFood(Member2 findMember) {
        findMember.getFavoriteFoods().remove("치킨");
        findMember.getFavoriteFoods().add("한식");
    }

    private static Member2 updateHomeAddress(Member2 findMember) {
        // 값 타입 업데이트
        Address homeAddress = findMember.getHomeAddress();
        findMember.updateHomeAddress(new Address("newCity", homeAddress.getStreet(), homeAddress.getZipcode()));
        return findMember;
    }

    private static void findMemberInfo(Member2 findMember) {
        List<AddressEntity> addressHistory = findMember.getAddressHistory();
        for (AddressEntity findAddress : addressHistory) {
            Address address = findAddress.getAddress();
            System.out.println("findAddress = " + address.getCity());
        }

        findMember.getFavoriteFoods().forEach(System.out::println);
    }
}
