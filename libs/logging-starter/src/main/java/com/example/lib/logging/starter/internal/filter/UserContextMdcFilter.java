package com.example.lib.logging.starter.internal.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.common.core.context.user.UserContextHolder;
import com.example.lib.logging.core.constant.LoggingConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 사용자 컨텍스트 정보를 MDC에 기록하는 필터입니다.
 */
public class UserContextMdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        UserContext context = UserContextHolder.getContext();
        
        // 시큐리티 모듈이 없을 수도 있으므로 null 체크를 유지합니다.
        if (context != null && !context.user().isGuest()) {
            MDC.put(LoggingConstants.MDC_USER_ID_KEY, String.valueOf(context.user().userId()));
        }
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(LoggingConstants.MDC_USER_ID_KEY);
        }
    }
}
