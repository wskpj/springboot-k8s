package com.example.springboot_app.infrastructure.security.policy;

import jakarta.servlet.http.HttpServletRequest;

public interface RateLimitPolicy {
    /**
     * 해당 요청에 이 정책을 적용할 수 있는지 확인합니다.
     */
    boolean supports(HttpServletRequest request);

    /**
     * 허용되는 최대 요청 횟수를 반환합니다.
     */
    int getLimit();

    /**
     * 제한 주기(초)를 반환합니다.
     */
    long getDuration();
}
