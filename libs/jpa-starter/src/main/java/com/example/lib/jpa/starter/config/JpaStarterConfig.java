package com.example.lib.jpa.starter.config;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.lib.event.core.EventSource;
import com.example.lib.jpa.starter.aspect.LockConflictRetryAspect;
import com.example.lib.jpa.starter.event.JpaEventSourceHolder;

/**
 * JPA 스타터의 기본 설정을 담은 추상 클래스입니다.
 * 애플리케이션에서 수동 설정을 원할 경우 이 클래스를 상속받아 @Configuration을 붙이세요.
 */
@EnableJpaAuditing(auditorAwareRef = "jpaAuditorAware", dateTimeProviderRef = "dateTimeProvider")
public abstract class JpaStarterConfig {

    private final EventSource eventSource;

    protected JpaStarterConfig(EventSource eventSource) {
        if (eventSource == null) {
            throw new IllegalArgumentException("[JpaStarterConfig] EventSource must not be null. Please provide an EventSource.");
        }
        this.eventSource = eventSource;
        JpaEventSourceHolder.setEventSource(eventSource);
    }

    @Bean
    public LockConflictRetryAspect lockConflictRetryAspect(ApplicationEventPublisher eventPublisher) {
        return new LockConflictRetryAspect(eventSource, eventPublisher);
    }

    @Bean
    public AuditorAware<String> jpaAuditorAware() {
        return new JpaAuditorAware();
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
