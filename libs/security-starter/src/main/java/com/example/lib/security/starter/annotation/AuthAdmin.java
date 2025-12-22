package com.example.lib.security.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

<<<<<<<< HEAD:src/main/java/com/example/springboot_app/domain/auth/annotations/AuthAdmin.java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthAdmin {}
========
/**
 * 관리자 권한이 필요한 API임을 표시하는 어노테이션입니다.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthAdmin {
}
>>>>>>>> 5251f70 (squash with security starter):libs/security-starter/src/main/java/com/example/lib/security/starter/annotation/AuthAdmin.java
