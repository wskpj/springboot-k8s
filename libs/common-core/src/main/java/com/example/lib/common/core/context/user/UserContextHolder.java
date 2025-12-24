package com.example.lib.common.core.context.user;

/**
 * 현재 스레드의 사용자 컨텍스트 홀더
 */
public class UserContextHolder {
    
    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(UserContext userContext) {
        CONTEXT.set(userContext);
    }

    /**
     * 현재 컨텍스트를 반환합니다.
     */
    public static CurrentUser getContext() {
        UserContext context = CONTEXT.get();
        return context != null ? context : UserContext.guest();
    }

    public static void clearContext() {
        CONTEXT.remove();
    }
}
