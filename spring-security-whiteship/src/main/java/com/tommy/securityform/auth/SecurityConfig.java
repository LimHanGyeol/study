package com.tommy.securityform.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**").permitAll() // "/", "/info" 로 오는 요청은 인증을 거치지 않아도 상관 없다.
                .mvcMatchers("/admin").hasRole("ADMIN") // "/admin" 은 ADMIN 권한이 있어야 접근 가능하다.
                .anyRequest().authenticated(); // 기타 등등에 대한 요청들은 인증만 하면 접근이 가능하다.

        // and() 로 체이닝을 하지 않을 수도 있다.
        http.formLogin(); // 그리고 폼 로그인을 사용할 것이다.
        http.httpBasic(); // 그리고 httpBasic 도 사용한다.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() // 인메모리 방식으로 원하는 유저정보를 임의로 설정할 수 있다.
                .withUser("hangyeol").password("{noop}123").roles("USER")
                .and()  // noop 은 시큐리티 5부터 사용되는 기본 패스워드 인코더이다.
                .withUser("admin").password("{noop}!@#").roles("ADMIN");
    }
}
