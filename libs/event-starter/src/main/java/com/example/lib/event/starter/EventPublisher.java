package com.example.lib.event.starter;

import com.example.lib.event.core.DomainEvent;

/**
 * 도메인 이벤트를 발행하는 인터페이스입니다.
 */
public interface EventPublisher {
    
    /**
     * 이벤트를 발행합니다.
     */
    void publish(DomainEvent event);
}
