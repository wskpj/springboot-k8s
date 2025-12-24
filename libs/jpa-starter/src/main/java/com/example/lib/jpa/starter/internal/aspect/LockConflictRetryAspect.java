package com.example.lib.jpa.starter.internal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.dao.OptimisticLockingFailureException;

import com.example.lib.event.core.EventSource;
import com.example.lib.event.core.EventType;
import com.example.lib.jpa.core.annotation.OptimisticLock;
import com.example.lib.jpa.starter.internal.event.LockConflictEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @OptimisticLock 어노테이션이 붙은 메서드에서 낙관적 락 예외 발생 시 재시도를 수행하는 Aspect입니다.
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class LockConflictRetryAspect implements Ordered {

    private final EventSource eventSource;
    private final EventType eventType;
    private final ApplicationEventPublisher eventPublisher;

    @Around("@annotation(optimisticLock)")
    public Object doRetry(ProceedingJoinPoint joinPoint, OptimisticLock optimisticLock) throws Throwable {
        try {
            if (!optimisticLock.retry()) {
                return joinPoint.proceed();
            }
        } catch (OptimisticLockingFailureException e) {
            eventPublisher.publishEvent(new LockConflictEvent(
                eventType,
                eventSource,
                joinPoint.getSignature().toShortString(),
                1,
                false,
                e
            ));
            throw e;
        }

        int maxAttempts = optimisticLock.maxAttempts();
        long backoff = optimisticLock.backoff();
        OptimisticLockingFailureException lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockingFailureException e) {
                lastException = e;
                log.warn("[LockConflictRetry] Attempt {}/{} failed. Method: {}", 
                    attempt, maxAttempts, joinPoint.getSignature().toShortString());
                
                if (attempt < maxAttempts) {
                    Thread.sleep(backoff);
                }
            }
        }

        log.error("[LockConflictRetry] All {} attempts failed. Giving up.", maxAttempts);
        
        eventPublisher.publishEvent(new LockConflictEvent(
            eventType,
            eventSource,
            joinPoint.getSignature().toShortString(),
            maxAttempts,
            true,
            lastException
        ));
        
        throw lastException;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10;
    }
}
