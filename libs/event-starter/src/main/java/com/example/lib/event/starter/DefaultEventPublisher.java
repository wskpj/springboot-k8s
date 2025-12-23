package com.example.lib.event.starter;

import org.springframework.context.ApplicationEventPublisher;

/**
 * 특별한 설정이 없을 때 사용되는 기본 이벤트 발행 구현체입니다.
 */
public final class DefaultEventPublisher extends BaseEventPublisher {

    public DefaultEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }
}
