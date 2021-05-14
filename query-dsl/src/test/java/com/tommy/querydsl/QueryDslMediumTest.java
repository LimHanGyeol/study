package com.tommy.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tommy.querydsl.member.Member;
import com.tommy.querydsl.member.MemberDto;
import com.tommy.querydsl.member.QMember;
import com.tommy.querydsl.member.QMemberDto;
import com.tommy.querydsl.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.tommy.querydsl.member.QMember.member;

@SpringBootTest
@Transactional
public class QueryDslMediumTest {

    @Autowired
    private EntityManager entityManager;

    private Member member1;
    private Member member2;
    private Member member3;
    private Member member4;
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(entityManager);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        member1 = new Member("member1", 10);
        member1.participateTeam(teamA);
        entityManager.persist(member1);

        member2 = new Member("member2", 20);
        member2.participateTeam(teamA);
        entityManager.persist(member2);

        member3 = new Member("member3", 30);
        member3.participateTeam(teamB);
        entityManager.persist(member3);

        member4 = new Member("member4", 40);
        member4.participateTeam(teamB);
        entityManager.persist(member4);
    }

    @Test
    void simpleProjection() {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();
    }

    @Test
    void tupleProjection() {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    /**
     * JPA에서 DTO를 조회할 때는 JPQL로 NEW 명령어를 사용해야 한다.
     * 그 과정에서 DTO의 package 이름을 다 적어줘야해서 지저분하다.
     */
    @Test
    void findDtoByJPQL() {
        List<MemberDto> result = entityManager.createQuery(
                "SELECT NEW com.tommy.querydsl.member.MemberDto(m.username, m.age) FROM Member m", MemberDto.class
        ).getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * Java Bean을 이용한 방식은 getter, setter, noArgConstructor 가 필요하다.
     */
    @Test
    void findDtoByQueryDslSetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto dto : result) {
            System.out.println("dto = " + dto);
        }
    }

    /**
     * 필드를 이용한 DTO 조회는 getter, setter를 안써도 된다.
     * 바로 필드에 접근하여 값을 주입한다.
     */
    @Test
    void findDtoByQueryDslField() {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto dto : result) {
            System.out.println("dto = " + dto);
        }
    }

    /**
     * 생성자 주입 방식은 setter를 쓰지 않아도 된다.
     * 대신 생성자의 파라미터 순서에 맞게 데이터를 맞춰야 한다.
     */
    @Test
    void findDtoByQueryDslConstructor() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto dto : result) {
            System.out.println("dto = " + dto);
        }
    }

    /**
     * 별칭이 다를 수 있다.
     * 다른 DTO를 조회하거나 할 경우 조회하는 필드와 리턴 타입의 필드 명이 같아야 한다.
     * 그렇지 않으면 매칭이 안되어 값이 무시된다.
     * 이럴 경우 as로 alias 별칭을 주어 값을 맞출 수 있다.
     *
     * 서브쿠리를 사용하게 될 경우 ExpressionUtils로 한번 감싸어 alias를 줄 수 있다.
     */
    @Test
    void findUserDto() {
        QMember subMember = new QMember("subMember");

        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"), // ExpressionUtils.as(member.username, "name")
                        ExpressionUtils.as(
                                select(subMember.age.max())
                                        .from(subMember), "age")
                ))
                .from(member)
                .fetch();

        for (UserDto dto : result) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void findUserDtoByConstructor() {
        List<UserDto> result = queryFactory
                .select(Projections.constructor(UserDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (UserDto dto : result) {
            System.out.println("dto = " + dto);
        }
    }

    /**
     * @QueryProjection을 사용하면 Q파일로 만들어주어 바로 접근할 수 있다.
     * 생성자 방식으로 주입하게 되면 컴파일 단계에서 오류를 잡을 수 없다. 런타임에서 에러가 된다.
     * QueryProjection은 생성자에서 잘못된 값을 주입하면 컴파일 에러가 난다.
     *
     * 하지만 Q 파일을 생성해야 한다는 점과 DTO가 QueryDSL에 대해 의존하게 된다는 단점이 있다.
     * DTO같은 경우에는 domain layer에서 사용 자유롭다.
     * 흘러가는 DTO 안에 QueryDSL이 들어 있어 좋지 않다.
     *
     * DTO를 깔끔하게 가져가고 싶으면 QueryProjection을 지양하고,
     * 생성자, 필드 방식을 주로 사용한다.
     */
    @Test
    void findDtoByQueryProjection() {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
}
