package com.example.lib.event.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 인터페이스 기반의 EventType을 간단하게 생성할 수 있는 구현체입니다.
 * 특정 Enum에 의존하고 싶지 않은 라이브러리 모듈 등에서 사용합니다.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultEventType implements EventType {

    private final String code;
    private final String description;

    public static DefaultEventType of(String code, String description) {
        return new DefaultEventType(code, description);
    }
}
