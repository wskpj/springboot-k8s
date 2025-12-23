package com.example.lib.jpa.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.lib.jpa.starter.aspect.OptimisticLockRetryAspect;

/**
 * JPA 스타터의 기본 설정을 담은 추상 클래스입니다.
 * 애플리케이션에서 수동 설정을 원할 경우 이 클래스를 상속받아 @Configuration을 붙이세요.
 */
@EnableJpaAuditing(auditorAwareRef = "jpaAuditorAware")
public abstract class JpaStarterConfig {

    @Bean
    public OptimisticLockRetryAspect optimisticLockRetryAspect() {
        return new OptimisticLockRetryAspect();
    }

    @Bean
    public AuditorAware<String> jpaAuditorAware() {
        return new JpaAuditorAware();
    }
}
