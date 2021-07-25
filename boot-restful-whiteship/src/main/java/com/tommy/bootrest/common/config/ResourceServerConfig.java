package com.tommy.bootrest.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * 현재 강의에서 사용한 spring-security-oauth2-autoconfigure 의존성은 사용되지 않는다.
 * Spring Boot 1.5에서 2.0으로 넘어갈 때 호환성을 위해 사용되었으며,
 * Security5.0 으로 넘어간 이후에는 많은 기능이 Deprecated 되었다.
 * 그리고 Spring Security Team은 AuthorizationServer 프로젝트를 폐기하기로 결정했다.
 * 하지만 많은 피드백으로 인해 커뮤니티 프로젝트로 spring-security-oauth2-authorization-server 프로젝트가
 * 나오게 되었고, AuthorizationServer를 구성해야할 경우 해당 의존성을 이용해야 한다.
 * 현재는 강의를 수행하고자 oauth2-autoconfigure 의존성을 사용했지만,
 * 나중에 리팩토링 혹은 oauth server를 구성할 필요가 있을 경우 oauth2-authorization-server 의존성 사용을 해야 할 것 이다.
 *
 * 참조
 * https://spring.io/blog/2021/05/10/spring-authorization-server-0-1-1-available-now
 * https://github.com/spring-projects-experimental/spring-authorization-server
 * https://www.baeldung.com/spring-security-oauth-auth-server
 * https://www.baeldung.com/spring-security-oauth-resource-server
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous()
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());

    }
}
