package com.example.lib.event.core;

import lombok.Getter;

/**
 * 비즈니스 로직(도메인)에서 발생하는 이벤트의 기반 클래스입니다.
 */
@Getter
public abstract non-sealed class BaseDomainEvent extends BaseEvent {

    protected BaseDomainEvent(String source) {
        super(source);
    }
}
