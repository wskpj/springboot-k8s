package com.example.lib.common.core.context;

/**
 * 현재 스레드의 TraceContext를 관리하는 홀더 클래스입니다.
 */
public class TraceContextHolder {
    
    private static final ThreadLocal<TraceContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(TraceContext traceContext) {
        CONTEXT.set(traceContext);
    }

    public static TraceContext getContext() {
        return CONTEXT.get();
    }

    public static String getTraceId() {
        TraceContext context = CONTEXT.get();
        return context != null ? context.traceId() : null;
    }

    public static void clearContext() {
        CONTEXT.remove();
    }
}
