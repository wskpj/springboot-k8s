package com.example.lib.security.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 본인의 리소스에만 접근 가능한 API임을 표시하는 어노테이션입니다.
 * (예: 자신의 정보 수정 등)
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthSelf {
    /**
     * 검증에 사용할 대상 ID의 경로 변수(PathVariable) 이름입니다.
     * 기본값은 "id" 입니다.
     */
    String value() default "id";
}
