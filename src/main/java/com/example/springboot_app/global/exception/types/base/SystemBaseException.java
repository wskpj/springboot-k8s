package com.example.springboot_app.global.exception.types.base;

import com.example.springboot_app.global.exception.enums.ErrorType;

/**
 * 시스템 예외 (예: 예상치 못한 널 포인트 예외 등)
 * 로그 레벨: ERROR
 */
public abstract class SystemBaseException extends BaseException {

    public SystemBaseException(ErrorType errorType) {
        super(errorType);
    }

    public SystemBaseException(ErrorType errorType, Object details) {
        super(errorType, details);
    }
}
