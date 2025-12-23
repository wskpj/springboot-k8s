package com.example.lib.event.core;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.lib.common.core.context.TraceContextHolder;

import lombok.Getter;

/**
 * 모든 시스템/도메인 이벤트의 최상위 추상 기반 클래스입니다.
 */
@Getter
public abstract sealed class BaseEvent permits BaseDomainEvent, BaseSystemEvent {

    private final UUID eventId;
    private final String eventType;
    private final String source;
    private final String traceId;
    private final LocalDateTime timestamp;

    protected BaseEvent(String source) {
        this.eventId = UUID.randomUUID();
        this.eventType = this.getClass().getSimpleName();
        this.source = source;
        this.traceId = TraceContextHolder.getTraceId();
        this.timestamp = LocalDateTime.now();
    }
}
