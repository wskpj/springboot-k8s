package com.example.lib.logging.starter.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lib.common.core.context.trace.TraceContext;
import com.example.lib.common.core.context.trace.TraceContextHolder;
import com.example.lib.logging.starter.constant.LoggingConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 모든 요청에 대해 고유한 Trace ID를 컨텍스트와 MDC에 삽입합니다.
 */
public class MdcLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String traceId = request.getHeader("X-Trace-Id");
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }

        // 1. Context에 데이터 저장 (데이터 본질)
        TraceContextHolder.setContext(TraceContext.create(traceId));
        
        // 2. Logger(MDC)에 데이터 복사 (표현)
        MDC.put(LoggingConstants.MDC_TRACE_ID_KEY, traceId);
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(LoggingConstants.MDC_TRACE_ID_KEY);
            TraceContextHolder.clearContext();
        }
    }
}
