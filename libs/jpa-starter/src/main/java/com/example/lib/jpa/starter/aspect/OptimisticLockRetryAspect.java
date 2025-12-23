package com.example.lib.jpa.starter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import com.example.lib.event.core.OptimisticLockConflictEvent;
import com.example.lib.jpa.core.annotation.OptimisticLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @OptimisticLock 어노테이션이 붙은 메서드에서 낙관적 락 예외 발생 시 재시도를 수행하는 Aspect입니다.
 * 트랜잭션(@Transactional)보다 먼저 실행되어야 하므로 Ordered.LOWEST_PRECEDENCE - 10 수준의 높은 우선순위를 가집니다.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OptimisticLockRetryAspect implements Ordered {

    private final ApplicationEventPublisher eventPublisher;

    @Around("@annotation(optimisticLock)")
    public Object doRetry(ProceedingJoinPoint joinPoint, OptimisticLock optimisticLock) throws Throwable {
        try {
            if (!optimisticLock.retry()) {
                return joinPoint.proceed();
            }
        } catch (OptimisticLockingFailureException e) {
            // 재시도가 비활성화된 경우에도 충돌 이벤트 발행
            eventPublisher.publishEvent(new OptimisticLockConflictEvent(
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
                log.warn("[OptimisticLockRetry] Attempt {}/{} failed due to optimistic lock conflict. Method: {}", 
                    attempt, maxAttempts, joinPoint.getSignature().toShortString());
                
                if (attempt < maxAttempts) {
                    Thread.sleep(backoff);
                }
            }
        }

        log.error("[OptimisticLockRetry] All {} attempts failed. Giving up.", maxAttempts);
        
        // 최종 실패 시 이벤트 발행
        eventPublisher.publishEvent(new OptimisticLockConflictEvent(
            joinPoint.getSignature().toShortString(),
            maxAttempts,
            true,
            lastException
        ));
        
        throw lastException;
    }

    @Override
    public int getOrder() {
        // @Transactional(기본값: Ordered.LOWEST_PRECEDENCE)보다 바깥에서 실행되어야 함
        return Ordered.LOWEST_PRECEDENCE - 10;
    }
}
