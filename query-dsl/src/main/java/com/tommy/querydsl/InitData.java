package com.tommy.querydsl;

import com.tommy.querydsl.member.domain.Member;
import com.tommy.querydsl.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
@Profile("local")
@Component
public class InitData {

    private final InitMemberService initMemberService;

    @PostConstruct
    public void init() {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {

        @PersistenceContext
        private EntityManager entityManager;

        // Spring Lifecycle로 인해 PostConstruct와 Transanctional을 같이 사용할 수 없다.
        @Transactional
        public void init() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            entityManager.persist(teamA);
            entityManager.persist(teamB);

            for (int i = 0; i < 100; i++) {
                Team selectedTeam = teamB;
                if (i % 2 == 0) {
                    selectedTeam = teamA;
                }
                Member member = new Member("member" + i, i);
                member.participateTeam(selectedTeam);
                entityManager.persist(member);
            }
        }
    }
}
