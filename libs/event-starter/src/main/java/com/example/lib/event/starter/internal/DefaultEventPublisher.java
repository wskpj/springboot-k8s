package com.example.lib.event.starter.internal;

import org.springframework.context.ApplicationEventPublisher;
import com.example.lib.event.core.BaseEvent;
import com.example.lib.event.core.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring의 ApplicationEventPublisher를 사용하여 이벤트를 발행하는 기본 구현체입니다.
 */
@Slf4j
@RequiredArgsConstructor
public final class DefaultEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(BaseEvent event) {
        log.debug("[EventPublisher] Type: {}, ID: {}", event.getEventType(), event.getEventId());
        applicationEventPublisher.publishEvent(event);
    }
}
