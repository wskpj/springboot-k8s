package com.example.lib.logging.starter.decorator;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import com.example.lib.common.core.context.trace.TraceContext;
import com.example.lib.common.core.context.trace.TraceContextHolder;
import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.common.core.context.user.UserContextHolder;

/**
 * 부모 스레드의 Context와 MDC를 자식 스레드(비동기 작업)로 전파하는 데코레이터입니다.
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 부모 스레드(HTTP 요청 스레드 등)의 컨텍스트 캡처
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        UserContext userContext = (UserContext) UserContextHolder.getContext();
        TraceContext traceContext = (TraceContext) TraceContextHolder.getContext();

        return () -> {
            try {
                // 자식 스레드(비동기 실행 스레드)에 컨텍스트 설정
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
                // 자식 스레드의 컨텍스트 정리
                MDC.clear();
                UserContextHolder.clearContext();
                TraceContextHolder.clearContext();
            }
        };
    }
}
