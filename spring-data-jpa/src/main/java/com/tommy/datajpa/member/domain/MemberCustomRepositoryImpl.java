package com.tommy.datajpa.member.domain;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * CustomRepository 구현체의 규칙이 하나 있다.
 * 상속 받는 인터페이스 이름은 상관이 없지만,
 * 커스텀할 상위 인터페이스와 이름을 맞추고 마지막에 Impl을 작성해야 한다.
 */
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<Member> findMemberCustom() {
        return entityManager.createQuery(
                "SELECT m FROM Member m", Member.class)
                .getResultList();
    }
}
