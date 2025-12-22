package com.example.springboot_app.global.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.springboot_app.global.context.UserContext;
import com.example.springboot_app.global.context.UserContextHolder;
import com.example.springboot_app.global.context.UserContextResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 컨텍스트를 MDC 및 UserContextHolder에 추가합니다.
 */
@RequiredArgsConstructor
public class UserContextMdcFilter extends OncePerRequestFilter {

    public static final String MDC_USER_INFO_KEY = "userInfo";
    private final UserContextResolver userContextResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        UserContext context = userContextResolver.getCurrentUserContext()
                .orElseGet(UserContext::guest);

        // 1. MDC 설정 (로깅용)
        MDC.put(MDC_USER_INFO_KEY, context.userId());
        
        // 2. UserContextHolder 설정 (도메인/글로벌 코드 접근용)
        UserContextHolder.setContext(context);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_USER_INFO_KEY);
            UserContextHolder.clearContext();
        }
    }
}
