package com.example.lib.security.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

<<<<<<<< HEAD:src/main/java/com/example/springboot_app/domain/auth/annotations/AuthPublic.java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPublic {}
========
/**
 * 로그인이 필요하지 않은 공개 API임을 표시하는 어노테이션입니다.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPublic {
}
>>>>>>>> 5251f70 (squash with security starter):libs/security-starter/src/main/java/com/example/lib/security/starter/annotation/AuthPublic.java
