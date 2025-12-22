package com.example.lib.common.core.context;

/**
 * 현재 스레드의 UserContext 홀더
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
