package com.example.springboot_app.global.context;

/**
 * 현재 스레드의 UserContext를 관리하는 홀더.
 * ThreadLocal을 사용하여 요청 스레드 전반에서 사용자 정보를 공유합니다.
 */
public class UserContextHolder {
    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(UserContext userContext) {
        CONTEXT.set(userContext);
    }

    public static UserContext getContext() {
        UserContext context = CONTEXT.get();
        return context != null ? context : UserContext.guest();
    }

    public static void clearContext() {
        CONTEXT.remove();
    }
}
