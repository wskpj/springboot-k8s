package com.example.lib.common.core.context.trace;

/**
 * 현재 스레드의 TraceContext를 관리하는 홀더 클래스입니다.
 */
public class TraceContextHolder {
    
    private static final ThreadLocal<TraceContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(TraceContext traceContext) {
        CONTEXT.set(traceContext);
    }

    /**
     * 현재 컨텍스트를 반환합니다. 인터페이스로 반환하여 결합도를 낮춥니다.
     */
    public static CurrentTrace getContext() {
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
