package com.tommy.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class QueryDslApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        entityManager.persist(hello);

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QHello qHello = QHello.hello;

        Hello result = queryFactory
                .selectFrom(qHello)
                .fetchOne();

        assertThat(result).isEqualTo(hello);
        assertThat(result.getId()).isEqualTo(hello.getId());
    }

}
