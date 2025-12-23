package com.example.lib.jpa.starter.event;

import com.example.lib.event.core.EventSource;
import com.example.lib.event.core.EventType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JPA 관련 시스템 이벤트 타입을 정의하는 Enum입니다.
 */
@Getter
@RequiredArgsConstructor
public enum JpaEventType implements EventType {

    OPTIMISTIC_LOCK_CONFLICT("JPA_001", "낙관적 락 충돌 발생");

    private final String code;
    private final String description;

    @Override
    public EventSource getSource() {
        return JpaEventSourceHolder.getEventSource();
    }
}
