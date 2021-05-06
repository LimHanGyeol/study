package com.tommy.securityform.auth;

import com.tommy.securityform.FormAccessDeniedHandler;
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
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**", "/signup").permitAll() // "/", "/info" 로 오는 요청은 인증을 거치지 않아도 상관 없다.
                .mvcMatchers("/admin").hasRole("ADMIN") // "/admin" 은 ADMIN 권한이 있어야 접근 가능하다.
                .mvcMatchers("/user").hasRole("USER")
                .anyRequest().authenticated() // 기타 등등에 대한 요청들은 인증만 하면 접근이 가능하다.
                .expressionHandler(expressionHandler());

        // and() 로 체이닝을 하지 않을 수도 있다.
        http.formLogin() // 그리고 폼 로그인을 사용할 것이다.
                .loginPage("/login") // 이 필터를 추가하면 DefaultLogin/LogoutPageGeneratingFilter가 빠진다.
                .permitAll();

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
