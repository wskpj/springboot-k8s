package com.example.lib.event.starter;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.lib.event.core.BaseEvent;

import lombok.RequiredArgsConstructor;

/**
 * Spring의 ApplicationEventPublisher를 사용하는 이벤트 발행 구현체입니다.
 */
@Component
@RequiredArgsConstructor
public class SpringEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(BaseEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
