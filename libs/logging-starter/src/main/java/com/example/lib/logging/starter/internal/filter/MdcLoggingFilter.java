package com.example.lib.logging.starter.internal.filter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lib.common.core.context.trace.CurrentTrace;
import com.example.lib.common.core.context.trace.TraceContext;
import com.example.lib.common.core.context.trace.TraceContextHolder;
import com.example.lib.logging.core.constant.LoggingConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 모든 요청에 대해 Trace ID와 Span ID를 생성하고 MDC에 기록하는 필터입니다.
 * 컨텍스트 레코드를 생성하여 홀더에 저장합니다.
 */
public class MdcLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String traceId = request.getHeader(LoggingConstants.HEADER_TRACE_ID);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }

        String spanId = UUID.randomUUID().toString().substring(0, 8);
        
        // 코어의 표준 Default 레코드를 사용하여 컨텍스트 생성
        CurrentTrace traceData = new CurrentTrace.Default(
            traceId,
            spanId,
            null,
            true,
            OffsetDateTime.now()
        );
        
        TraceContextHolder.setContext(new TraceContext(traceData));

        MDC.put(LoggingConstants.MDC_TRACE_ID_KEY, traceId);
        MDC.put(LoggingConstants.MDC_SPAN_ID_KEY, spanId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
            TraceContextHolder.clearContext();
        }
    }
}
