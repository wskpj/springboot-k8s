package com.example.lib.event.core;

/**
 * 이벤트의 유형을 정의하는 인터페이스입니다.
 * 주로 도메인별 Enum으로 구현하여 사용합니다.
 */
public interface EventType {
    
    /**
     * 이벤트 코드
     */
    String getCode();

    /**
     * 이벤트 설명
     */
    String getDescription();

    /**
     * 이벤트 발생 소스
     */
    EventSource getSource();
}
