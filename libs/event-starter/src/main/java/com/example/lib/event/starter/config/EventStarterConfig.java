package com.example.lib.event.starter.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

import com.example.lib.event.starter.BaseEventPublisher;
import com.example.lib.event.starter.DefaultEventPublisher;

/**
 * 이벤트 시스템의 기본 설정을 담은 추상 클래스입니다.
 * 애플리케이션에서 이 설정을 커스터마이징하려면 이 클래스를 상속받아 @Configuration을 붙여 사용하세요.
 */
public abstract class EventStarterConfig {

    @Bean
    public BaseEventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultEventPublisher(applicationEventPublisher);
    }
}
