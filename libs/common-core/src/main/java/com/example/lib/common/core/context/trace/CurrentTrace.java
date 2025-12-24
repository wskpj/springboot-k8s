package com.example.lib.common.core.context.trace;

import java.time.OffsetDateTime;

/**
 * 현재 요청의 추적 정보를 제공하는 읽기 전용 인터페이스입니다.
 */
public interface CurrentTrace {
    String traceId();
    OffsetDateTime startTime();
}
