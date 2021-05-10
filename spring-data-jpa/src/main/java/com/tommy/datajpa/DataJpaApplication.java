package com.tommy.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Spring 사용시 이런 베이스 패키지 경로를 설정해 주어야 하는데 Spring Boot에서는 이를 자동으로 해준다.
// @EnableJpaRepositories(basePackages = "study.datajpa.repository")
@SpringBootApplication
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

}
