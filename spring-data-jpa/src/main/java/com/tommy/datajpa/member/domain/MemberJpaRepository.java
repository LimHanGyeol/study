package com.tommy.datajpa.member.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public List<Member> findAll() {
        return entityManager.createQuery(
                "SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findBy(Long id) {
        Member member = entityManager.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Member findById(Long id) {
        return entityManager.find(Member.class, id);
    }

    public void delete(Member member) {
        entityManager.remove(member);
    }

    public long count() {
        return entityManager.createQuery(
                "SELECT COUNT(m) FROM Member m", Long.class)
                .getSingleResult();
    }

    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
        return entityManager.createQuery(
                "SELECT m FROM Member m WHERE m.username = :username AND m.age > :age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }
}
