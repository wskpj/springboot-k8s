package com.example.lib.common.core.context.user;

/**
 * 사용자 문맥(UserContext)을 스레드 로컬로 관리하는 홀더 클래스입니다.
 */
public class UserContextHolder {

    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();
    private static final UserContext ANONYMOUS_CONTEXT = new UserContext(CurrentUser.anonymous());

    /**
     * 현재 스레드의 사용자 문맥을 반환합니다.
     * 설정된 문맥이 없으면 기본 익명 문맥을 반환합니다.
     */
    public static UserContext getContext() {
        UserContext context = CONTEXT.get();
        return (context != null) ? context : ANONYMOUS_CONTEXT;
    }

    public static void setContext(UserContext context) {
        CONTEXT.set(context);
    }

    public static void clearContext() {
        CONTEXT.remove();
    }
}
