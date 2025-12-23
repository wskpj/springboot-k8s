package com.example.springboot_app.global.event;

import com.example.lib.event.core.EventSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 애플리케이션 전체의 이벤트 발생 소스를 정의하는 Enum입니다.
 * 모든 도메인 및 라이브러리 소스를 여기서 통합 관리합니다.
 */
@Getter
@RequiredArgsConstructor
public enum ApplicationEventSource implements EventSource {

    // Domain
    COUPON("coupon", "쿠폰 도메인"),
    USER("user", "사용자 도메인"),

    // System
    JPA("jpa", "JPA 인프라");

    private final String name;
    private final String description;
}
