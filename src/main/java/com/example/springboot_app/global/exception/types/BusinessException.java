package com.example.springboot_app.global.exception.types;

import com.example.springboot_app.global.exception.enums.ErrorType;

/**
 * 비즈니스 로직 수준의 예외 (예: 재고 부족, 권한 없음 등)
 * 로그 레벨: WARN
 */
public class BusinessException extends BaseException {
    public BusinessException(ErrorType errorType) {
        super(errorType);
    }

    public BusinessException(ErrorType errorType, Object details) {
        super(errorType, details);
    }
}
