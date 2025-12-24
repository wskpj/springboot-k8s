package com.example.lib.common.core.context.trace;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 추적 문맥을 담는 규격화된 컨테이너 클래스입니다.
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class TraceContext {
    private final CurrentTrace trace;
}
