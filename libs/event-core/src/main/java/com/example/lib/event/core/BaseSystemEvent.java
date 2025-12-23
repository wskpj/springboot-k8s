package com.example.lib.event.core;

import lombok.Getter;

/**
 * 인프라나 시스템 수준에서 발생하는 이벤트의 기반 클래스입니다.
 */
@Getter
public abstract non-sealed class BaseSystemEvent extends BaseEvent {

    protected BaseSystemEvent(EventType eventType) {
        super(eventType);
    }
}
