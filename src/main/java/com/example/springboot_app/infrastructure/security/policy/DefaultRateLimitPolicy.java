package com.example.springboot_app.infrastructure.security.policy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
@Order(Integer.MAX_VALUE)
public class DefaultRateLimitPolicy implements RateLimitPolicy {

    private static final int DEFAULT_LIMIT = 500;
    private static final long DEFAULT_DURATION = 60L;

    @Override
    public boolean supports(HttpServletRequest request) {
        return true;
    }

    @Override
    public int getLimit() {
        return DEFAULT_LIMIT;
    }

    @Override
    public long getDuration() {
        return DEFAULT_DURATION;
    }
}
