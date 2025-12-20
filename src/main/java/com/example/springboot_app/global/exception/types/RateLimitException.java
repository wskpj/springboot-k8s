package com.example.springboot_app.global.exception.types;

import com.example.springboot_app.global.exception.enums.GlobalError;

/**
 * 호출 제한 초과 시 발생하는 예외
 * 로그 레벨: WARN
 */
public class RateLimitException extends BusinessException {

    public RateLimitException(String details) {
        super(GlobalError.TOO_MANY_REQUESTS, details);
    }
}
