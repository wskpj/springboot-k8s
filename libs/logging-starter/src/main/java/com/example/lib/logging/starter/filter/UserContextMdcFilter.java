package com.example.lib.logging.starter.filter;

import java.io.IOException;

import org.slf4j.MDC;

import com.example.lib.common.core.context.UserContext;
import com.example.lib.common.core.context.UserContextHolder;
import com.example.lib.logging.starter.constant.LoggingConstants;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * 현재 사용자의 ID를 로그 MDC에 삽입하여 사용자별 로그 추적을 가능하게 합니다.
 * 이제 시큐리티 프레임워크가 아닌, 도메인 UserContextHolder를 직접 참조합니다.
 */
public class UserContextMdcFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // 도메인 컨텍스트에서 유저 정보를 가져와 MDC에 설정 (시큐리티 의존성 제거)
        UserContext context = UserContextHolder.getContext();
        if (!context.isGuest()) {
            MDC.put(LoggingConstants.MDC_USER_ID_KEY, context.userId());
        }
        
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(LoggingConstants.MDC_USER_ID_KEY);
        }
    }
}
