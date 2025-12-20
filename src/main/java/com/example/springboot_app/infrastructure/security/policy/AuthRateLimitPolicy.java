package com.example.springboot_app.infrastructure.security.policy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
@Order(1)
public class AuthRateLimitPolicy implements RateLimitPolicy {

    private static final String AUTH_PATH_PREFIX = "/api/v1/auth";
    private static final int AUTH_LIMIT = 10;
    private static final long DURATION = 60L;

    @Override
    public boolean supports(HttpServletRequest request) {
        return request.getRequestURI().startsWith(AUTH_PATH_PREFIX);
    }

    @Override
    public int getLimit() {
        return AUTH_LIMIT;
    }

    @Override
    public long getDuration() {
        return DURATION;
    }
}
