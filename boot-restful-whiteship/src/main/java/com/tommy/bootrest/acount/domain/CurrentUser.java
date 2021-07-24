package com.tommy.bootrest.acount.domain;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}

/*
 * 로그인하지 않은 사용자가 접근할 경우 account를 null로 반환하기로 했다.
 * 이를 Null Object Pattern을 사용하여 AnonymousAccount의 형태로 바꿀 수 있다.
 */
