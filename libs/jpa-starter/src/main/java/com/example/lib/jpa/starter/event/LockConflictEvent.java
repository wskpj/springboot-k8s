package com.example.lib.jpa.starter.event;

import com.example.lib.event.core.BaseSystemEvent;
import com.example.lib.event.core.EventSource;
import lombok.Getter;

/**
 * 낙관적 락 충돌이 발생했을 때 발행되는 이벤트입니다.
 */
@Getter
public class LockConflictEvent extends BaseSystemEvent {

    private final String methodName;
    private final int totalAttempts;
    private final boolean retryEnabled;
    private final Throwable cause;

    public LockConflictEvent(String methodName, int totalAttempts, boolean retryEnabled, EventSource eventSource, Throwable cause) {
        super(JpaEventType.OPTIMISTIC_LOCK_CONFLICT);
        this.methodName = methodName;
        this.totalAttempts = totalAttempts;
        this.retryEnabled = retryEnabled;
        this.cause = cause;
    }
}
