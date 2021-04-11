package com.tommy.jpa.basic.jpql;

import javax.persistence.EntityManager;
import java.util.List;

public class Projection {

    // 프로젝션으로 반환한 객체도 영속성 컨텍스트의 관리 대상이 된다.
    public static void entityTypeProjection(EntityManager entityManager) {
        // 엔티티 프로젝션
        List<MemberJ> results = entityManager.createQuery("SELECT m FROM MemberJ AS m", MemberJ.class)
                .getResultList();

        // 엔티티 프로젝션
        // 해당 엔티티 프로젝션은 Join Query 로 나간다.
        // Join 은 튜닝 등 성능에 영향을 줄 수 있는 요소가 많아, 이렇게 작성하는 방식은 권장되지 않는다.
        // JPA에서 작동될 쿼리가 예측되지 않는다.
        entityManager.createQuery("SELECT m.teamJ FROM MemberJ AS m", TeamJ.class);
        // Join의 경우 아래와 같이 작성하는 것을 권장한다.
        entityManager.createQuery("SELECT t FROM MemberJ AS m JOIN m.teamJ AS t", TeamJ.class);
    }

    public static void embeddedTypeProjection(EntityManager entityManager) {
        // 임베디드 타입 프로젝션
        // 해당 프로젝션은 소속 엔티티를 명시해줘야 하는 값 타입의 한계가 있다.
        entityManager.createQuery("SELECT o.address FROM OrderJ AS o", AddressJ.class);
    }

    public static void scalaTypeProjection(EntityManager entityManager) {
        // 스칼라 타입 프로젝션, 타입이 들어가지 않는다.
        // 해당 타입이 일반 SQL과 거의 같다고 보면 된다.
        // 여기서 응답 타입이 2개인데 가져올 방법이 고민될 수 있다.

        // 1. Query 타입으로 가져올 수 있다. 내부적으로 Object[] 로 이루어지고, 여기서는 0=username, 1=age 가 들어있다.
        List results1 = entityManager.createQuery("SELECT DISTINCT m.username, m.age FROM MemberJ AS m")
                .getResultList();

        Object object = results1.get(0);
        Object[] result = (Object[]) object;

        // 2. Object[] 타입으로 List를 명시할 수 있다.
        // 그러면 1번의 Object[] 타입으로 캐스팅 하는 과정이 생략된다.
        // 나중에 QueryDSL로 극복이 가능하다.
        List<Object[]> results2 = entityManager.createQuery("SELECT DISTINCT m.username, m.age FROM MemberJ AS m")
                .getResultList();

        // 3. DTO로 바로 조회
        // 이 방법이 제일 깔끔하지만 패키지 경로를 다 적어야 하는 단점이 있다.
        List<MemberDTO> resultList = entityManager.createQuery("SELECT DISTINCT new com.tommy.jpa.basic.jpql.MemberDTO(m.username, m.age) FROM MemberJ AS m", MemberDTO.class)
                .getResultList();

        MemberDTO memberDTO = resultList.get(0);
    }
}
