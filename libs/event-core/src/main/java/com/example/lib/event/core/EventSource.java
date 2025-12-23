package com.example.lib.event.core;

/**
 * 이벤트가 발생한 소스(발행처)를 정의하는 인터페이스입니다.
 * 도메인 또는 인프라 컴포넌트 단위로 구현합니다.
 */
public interface EventSource {
    
    /**
     * 소스 식별자
     */
    String getName();
}
