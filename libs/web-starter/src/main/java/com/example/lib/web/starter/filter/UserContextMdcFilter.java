package com.example.lib.web.starter.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.example.lib.common.core.context.UserContextResolver;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 현재 사용자의 ID를 로그 MDC에 삽입하여 사용자별 로그 추적을 가능하게 합니다.
 */
@Component
@RequiredArgsConstructor
public class UserContextMdcFilter implements Filter {

    private final UserContextResolver userContextResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        userContextResolver.getCurrentUserContext()
                .ifPresent(ctx -> MDC.put("userId", ctx.userId()));
        
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("userId");
        }
    }
}
