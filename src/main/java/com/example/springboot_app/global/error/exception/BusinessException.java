package com.example.springboot_app.global.error.exception;

import com.example.springboot_app.global.error.ErrorCode;

/**
 * 비즈니스 로직 수준의 예외 (예: 재고 부족, 권한 없음 등)
 * 로그 레벨: WARN
 */
public class BusinessException extends BaseException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(ErrorCode errorCode, Object details) {
        super(errorCode, details);
    }
}
