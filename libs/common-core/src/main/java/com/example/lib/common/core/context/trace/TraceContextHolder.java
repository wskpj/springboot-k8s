package com.example.lib.common.core.context.trace;

/**
 * 추적 문맥(TraceContext)을 스레드 로컬로 관리하는 홀더 클래스입니다.
 */
public class TraceContextHolder {

    private static final ThreadLocal<TraceContext> CONTEXT = new ThreadLocal<>();
    private static final TraceContext ANONYMOUS_CONTEXT = new TraceContext(CurrentTrace.anonymous());

    /**
     * 현재 스레드의 추적 문맥을 반환합니다.
     * 설정된 문맥이 없으면 기본 익명 문맥을 반환합니다.
     */
    public static TraceContext getContext() {
        TraceContext context = CONTEXT.get();
        return (context != null) ? context : ANONYMOUS_CONTEXT;
    }

    public static void setContext(TraceContext context) {
        CONTEXT.set(context);
    }

    public static void clearContext() {
        CONTEXT.remove();
    }

    /**
     * 현재 추적 ID를 반환합니다. 문맥이 없으면 "N/A"를 반환합니다.
     */
    public static String getTraceId() {
        return getContext().trace().traceId();
    }
}
