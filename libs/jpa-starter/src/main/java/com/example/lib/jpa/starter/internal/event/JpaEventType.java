package com.example.lib.jpa.starter.internal.event;

import com.example.lib.event.core.EventType;
import lombok.RequiredArgsConstructor;

/**
 * JPA 인프라에서 발생하는 이벤트 유형을 정의합니다.
 */
@RequiredArgsConstructor
public enum JpaEventType implements EventType {
    LOCK_CONFLICT("JPA_LOCK_CONFLICT", "낙관적 락 충돌 발생");

    private final String code;
    private final String description;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
