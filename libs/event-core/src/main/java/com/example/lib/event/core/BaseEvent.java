package com.example.lib.event.core;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.lib.common.core.context.trace.TraceContextHolder;

import lombok.Getter;

/**
 * 모든 시스템/도메인 이벤트의 최상위 추상 기반 클래스입니다.
 */
@Getter
public abstract sealed class BaseEvent permits BaseDomainEvent, BaseSystemEvent {

    private final UUID eventId;
    private final EventType eventType;
    private final EventSource eventSource;
    private final String traceId;
    private final OffsetDateTime timestamp;

    protected BaseEvent(EventType eventType, EventSource eventSource) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.eventSource = eventSource;
        this.traceId = TraceContextHolder.getTraceId();
        this.timestamp = OffsetDateTime.now();
    }
}
