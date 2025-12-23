package com.example.lib.event.starter;

import org.springframework.context.ApplicationEventPublisher;
import com.example.lib.event.core.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 이벤트를 발행하는 기본 클래스입니다.
 * 기본적으로 Spring의 ApplicationEventPublisher를 사용하며,
 * 특수한 발행 로직(Kafka, Redis 등)이 필요할 경우 이 클래스를 상속받아 확장합니다.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 이벤트를 발행합니다.
     */
    public void publish(BaseEvent event) {
        log.debug("[EventPublish] Type: {}, ID: {}", event.getEventType(), event.getEventId());
        applicationEventPublisher.publishEvent(event);
    }
}
