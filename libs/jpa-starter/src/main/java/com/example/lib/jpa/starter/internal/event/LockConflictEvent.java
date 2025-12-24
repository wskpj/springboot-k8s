package com.example.lib.jpa.starter.internal.event;

import com.example.lib.event.core.BaseSystemEvent;
import com.example.lib.event.core.EventSource;
import com.example.lib.event.core.EventType;
import lombok.Getter;

/**
 * 낙관적 락 충돌 발생 시 발생하는 이벤트입니다.
 */
@Getter
public class LockConflictEvent extends BaseSystemEvent {

    private final String methodName;
    private final int attempts;
    private final boolean fatal;
    private final Throwable cause;

    public LockConflictEvent(EventType eventType, EventSource eventSource, 
                             String methodName, int attempts, boolean fatal, Throwable cause) {
        super(eventType, eventSource);
        this.methodName = methodName;
        this.attempts = attempts;
        this.fatal = fatal;
        this.cause = cause;
    }
}
