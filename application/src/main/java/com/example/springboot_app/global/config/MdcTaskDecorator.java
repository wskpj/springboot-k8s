package com.example.springboot_app.global.config;

import com.example.lib.common.core.context.UserContext;
import com.example.lib.common.core.context.UserContextHolder;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
 * 부모 스레드의 MDC와 UserContext를 자식 스레드(비동기 작업)로 전파하는 데코레이터
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 부모 스레드(HTTP 요청 스레드 등)의 컨텍스트 캡처
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        UserContext userContext = UserContextHolder.getContext();

        return () -> {
            try {
                // 자식 스레드(비동기 실행 스레드)에 컨텍스트 설정
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                UserContextHolder.setContext(userContext);

                runnable.run();
            } finally {
                // 자식 스레드의 컨텍스트 정리
                MDC.clear();
                UserContextHolder.clearContext();
            }
        };
    }
}
