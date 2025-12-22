package com.example.lib.common.core.context;

import java.time.OffsetDateTime;

/**
 * 현재 요청의 추적(Trace) 컨텍스트를 담는 불변 객체입니다.
 */
public record TraceContext(
    String traceId,
    OffsetDateTime startTime
) {
    public static TraceContext create(String traceId) {
        return new TraceContext(traceId, OffsetDateTime.now());
    }
}
