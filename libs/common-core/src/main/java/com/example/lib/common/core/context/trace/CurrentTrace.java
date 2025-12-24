package com.example.lib.common.core.context.trace;

import java.time.OffsetDateTime;

/**
 * 추적 정보를 정의하는 인터페이스입니다.
 */
public interface CurrentTrace {
    String traceId();
    String spanId();
    String parentSpanId();
    boolean sampled();
    OffsetDateTime startTime();

    /**
     * 표준 추적 정보 구현체입니다.
     * 코어에서 이를 제공함으로써 스타터의 중복 구현을 방지합니다.
     */
    record Default(
        String traceId,
        String spanId,
        String parentSpanId,
        boolean sampled,
        OffsetDateTime startTime
    ) implements CurrentTrace {}

    /**
     * 비어있는 추적 정보를 반환합니다.
     */
    static CurrentTrace anonymous() {
        return new Default("N/A", "N/A", null, false, OffsetDateTime.now());
    }
}
