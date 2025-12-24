package com.example.lib.logging.core.constant;

/**
 * 로깅 및 추적에 사용되는 상수 모음입니다.
 */
public final class LoggingConstants {
    private LoggingConstants() {}
    
    public static final String MDC_USER_ID_KEY = "userId";

    // MDC Keys
    public static final String MDC_TRACE_ID_KEY = "traceId";
    public static final String MDC_SPAN_ID_KEY = "spanId";
    public static final String MDC_PARENT_SPAN_ID_KEY = "parentSpanId";
    public static final String MDC_SAMPLED_KEY = "sampled";
    public static final String MDC_CLIENT_IP_KEY = "clientIp";
    
    // Headers
    public static final String HEADER_TRACE_ID = "X-Trace-Id";
    public static final String HEADER_SPAN_ID = "X-Span-Id";
    public static final String HEADER_PARENT_SPAN_ID = "X-Parent-Span-Id";
    public static final String HEADER_SAMPLED = "X-Sampled";
}
