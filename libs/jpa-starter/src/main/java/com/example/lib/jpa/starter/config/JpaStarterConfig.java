package com.example.lib.jpa.starter.config;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.lib.event.core.DefaultEventType;
import com.example.lib.event.core.EventSource;
import com.example.lib.event.core.EventType;
import com.example.lib.jpa.starter.aspect.LockConflictRetryAspect;
/**
 * JPA 스타터의 기본 설정을 제공하는 자동 설정 클래스입니다.
 */
@AutoConfiguration
@EnableJpaAuditing(auditorAwareRef = "jpaAuditorAware", dateTimeProviderRef = "dateTimeProvider")
public class JpaStarterConfig {

    private final EventSource eventSource;

    public JpaStarterConfig(@Autowired(required = false) EventSource eventSource) {
        this.eventSource = eventSource;
    }

    @Bean
    @ConditionalOnMissingBean(name = "jpaLockConflictEventType")
    public EventType jpaLockConflictEventType() {
        return DefaultEventType.of("JPA_LOCK_CONFLICT", "낙관적 락 충돌 발생");
    }

    @Bean
    @ConditionalOnBean(EventSource.class)
    public LockConflictRetryAspect lockConflictRetryAspect(
            ApplicationEventPublisher eventPublisher,
            @Qualifier("jpaLockConflictEventType") EventType eventType) {
        return new LockConflictRetryAspect(eventSource, eventType, eventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean(name = "jpaAuditorAware")
    public AuditorAware<String> jpaAuditorAware() {
        return new JpaAuditorAware();
    }

    @Bean
    @ConditionalOnMissingBean(name = "dateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
