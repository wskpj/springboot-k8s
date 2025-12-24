package com.example.lib.jpa.starter.internal;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.lib.jpa.starter.internal.aspect.LockConflictRetryAspect;
import com.example.lib.jpa.starter.internal.event.JpaEventType;

/**
 * JPA 관련 인프라 설정을 제공하는 자동 설정 클래스입니다.
 */
@AutoConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "dateTimeProvider")
public class JpaStarterConfig {

    /**
     * 낙관적 락 재시도 Aspect를 빈으로 등록합니다.
     */
    @Bean
    @ConditionalOnMissingBean(LockConflictRetryAspect.class)
    public LockConflictRetryAspect lockConflictRetryAspect(
            ApplicationEventPublisher eventPublisher) {
        
        return new LockConflictRetryAspect(
            () -> "JPA_INFRA",
            JpaEventType.LOCK_CONFLICT,
            eventPublisher
        );
    }

    /**
     * JPA Auditing을 위한 AuditorAware 구현체를 빈으로 등록합니다.
     */
    @Bean(name = "auditorProvider")
    @ConditionalOnMissingBean(name = "auditorProvider")
    public AuditorAware<String> auditorProvider() {
        return new JpaAuditorAware();
    }

    /**
     * OffsetDateTime 지원을 위한 DateTimeProvider를 빈으로 등록합니다.
     */
    @Bean(name = "dateTimeProvider")
    @ConditionalOnMissingBean(name = "dateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
