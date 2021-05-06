package com.tommy.securityform.auth;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        return handler;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 동적으로 처리하는 리소스를 web으로 처리하는 것은 권장하지 않는다.
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class);

        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**", "/signup").permitAll() // "/", "/info" 로 오는 요청은 인증을 거치지 않아도 상관 없다.
                .mvcMatchers("/admin").hasRole("ADMIN") // "/admin" 은 ADMIN 권한이 있어야 접근 가능하다.
                .mvcMatchers("/user").hasAuthority("ROLE_USER") // hasAuthority가 hasRole의 상위 개념이다. hasAuthority를 사용하면 ROLE_까지 명시해야 한다.
                .anyRequest().authenticated() // 기타 등등에 대한 요청들은 인증만 하면 접근이 가능하다.
                .expressionHandler(expressionHandler());
                // .anonymous 로 익명 사용자에게만 허용할 수 있다.


        // and() 로 체이닝을 하지 않을 수도 있다.
        http.formLogin() // 그리고 폼 로그인을 사용할 것이다.
                .loginPage("/login") // 이 필터를 추가하면 DefaultLogin/LogoutPageGeneratingFilter가 빠진다.
                .permitAll();

//        http.rememberMe()
//                .userDetailsService(accountService)
//                .key("remember-me-sample");

        http.httpBasic(); // 그리고 httpBasic 도 사용한다.

        http.logout().logoutSuccessUrl("/");

        http.exceptionHandling()
                .accessDeniedHandler(new FormAccessDeniedHandler());
    }

    @Bean // default : bcrypt
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
