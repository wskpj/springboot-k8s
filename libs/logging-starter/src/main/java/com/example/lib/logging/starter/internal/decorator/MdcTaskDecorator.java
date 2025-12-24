package com.example.lib.logging.starter.internal.decorator;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import com.example.lib.common.core.context.trace.TraceContext;
import com.example.lib.common.core.context.trace.TraceContextHolder;
import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.common.core.context.user.UserContextHolder;

/**
 * 부모 스레드의 Context와 MDC를 자식 스레드(비동기 작업)로 전파하는 데코레이터입니다.
 * 컨테이너 레코드(TraceContext, UserContext)를 캡처하여 전달합니다.
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 부모 스레드의 상태 캡처
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        UserContext userContext = UserContextHolder.getContext();
        TraceContext traceContext = TraceContextHolder.getContext();

        return () -> {
            try {
                // 자식 스레드에 상태 복제
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                if (userContext != null) {
                    UserContextHolder.setContext(userContext);
                }
                if (traceContext != null) {
                    TraceContextHolder.setContext(traceContext);
                }
                runnable.run();
            } finally {
                // 자식 스레드 상태 정리
                MDC.clear();
                UserContextHolder.clearContext();
                TraceContextHolder.clearContext();
            }
        };
    }
}
