package com.example.lib.event.core;

/**
 * 이벤트를 발행하기 위한 공통 인터페이스입니다.
 * 스타터 모듈에서 이 인터페이스의 기본 구현을 제공합니다.
 */
public interface EventPublisher {
    
    /**
     * 이벤트를 발행합니다.
     * @param event 발행할 이벤트 객체
     */
    void publish(BaseEvent event);
}
