package com.example.lib.common.core.context.trace;

import java.time.OffsetDateTime;

/**
 * 현재 요청의 추적(Trace) 컨텍스트 데이터 객체 (Record)
 */
public record TraceContext(
    String traceId,
    OffsetDateTime startTime
) implements CurrentTrace {
    
    public static TraceContext create(String traceId) {
        return new TraceContext(traceId, OffsetDateTime.now());
    }
}
