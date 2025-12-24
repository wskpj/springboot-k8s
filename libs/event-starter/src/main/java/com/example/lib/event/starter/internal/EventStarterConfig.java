package com.example.lib.event.starter.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

import com.example.lib.event.core.EventPublisher;

/**
 * 이벤트 시스템의 기본 설정을 제공하는 자동 설정 클래스입니다.
 * 이 모듈의 핵심 빈 인터페이스인 EventPublisher의 기본 구현체를 등록합니다.
 */
@AutoConfiguration
public class EventStarterConfig {

    /**
     * EventPublisher의 기본 구현체를 빈으로 등록합니다.
     * @ConditionalOnMissingBean을 통해 애플리케이션에서 직접 EventPublisher 빈을 정의할 경우 오버라이드 가능합니다.
     */
    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultEventPublisher(applicationEventPublisher);
    }
}
