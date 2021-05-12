package com.tommy.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

// Spring 사용시 이런 베이스 패키지 경로를 설정해 주어야 하는데 Spring Boot에서는 이를 자동으로 해준다.
// @EnableJpaRepositories(basePackages = "study.datajpa.repository")
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvier() {
        // 예제로 사용하기 위해 UUID를 사용했다.
        // 실제 사용하게 될 경우 Session이나
        // SpringSecurityContext에서 Principal을 가져와 사용하게 될 것이다.
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
