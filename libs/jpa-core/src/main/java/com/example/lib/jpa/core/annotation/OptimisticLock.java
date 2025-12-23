package com.example.lib.jpa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 낙관적 락(Optimistic Lock) 관련 설정을 제어하는 어노테이션입니다.
 * 주로 메서드 단위의 재시도 정책을 설정하는 데 사용됩니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptimisticLock {

    /**
     * 낙관적 락 충돌 시 재시도 여부 (기본값: true)
     */
    boolean retry() default true;
    
    /**
     * 최대 재시도 횟수 (기본값: 3)
     */
    int maxAttempts() default 3;

    /**
     * 재시도 간 지연 시간 (ms, 기본값: 100ms)
     */
    long backoff() default 100;
}
