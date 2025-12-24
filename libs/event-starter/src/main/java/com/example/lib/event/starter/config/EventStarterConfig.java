package com.example.lib.event.starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

import com.example.lib.event.starter.BaseEventPublisher;
import com.example.lib.event.starter.DefaultEventPublisher;

/**
 * 이벤트 시스템의 기본 설정을 제공하는 자동 설정 클래스입니다.
 */
@AutoConfiguration
public class EventStarterConfig {

    @Bean
    @ConditionalOnMissingBean(BaseEventPublisher.class)
    public BaseEventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultEventPublisher(applicationEventPublisher);
    }
}
