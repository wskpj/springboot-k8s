package com.example.lib.security.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

<<<<<<<< HEAD:src/main/java/com/example/springboot_app/domain/auth/annotations/AuthSelf.java
@Target({ElementType.METHOD, ElementType.TYPE})
========
/**
 * 리소스의 소유자가 본인인지 검증이 필요한 API임을 표시하는 어노테이션입니다.
 * value는 소유자 ID가 포함된 경로 변수(PathVariable)의 이름입니다.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
>>>>>>>> 5251f70 (squash with security starter):libs/security-starter/src/main/java/com/example/lib/security/starter/annotation/AuthSelf.java
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthSelf {
    String value() default "id";
}
