package com.tommy.jpabook.bootjpaapplication.member.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long save(Member member) {
        entityManager.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }

    public List<Member> findAll() {
        return entityManager.createQuery("SELECT m FROM Member AS m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return entityManager.createQuery("SELECT m FROM Member AS m WHERE m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
