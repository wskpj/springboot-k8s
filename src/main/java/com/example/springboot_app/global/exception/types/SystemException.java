package com.example.springboot_app.global.exception.types;

import com.example.springboot_app.global.exception.enums.ErrorType;

/**
 * 일반적인 시스템 예외 (예: 예상치 못한 널 포인트 예외 등)
 * 로그 레벨: ERROR
 */
public class SystemException extends BaseException {
    public SystemException(ErrorType errorType) {
        super(errorType);
    }

    public SystemException(ErrorType errorType, Object details) {
        super(errorType, details);
    }
}
