package com.example.springboot_app.global.error.exception;

import com.example.springboot_app.global.error.ErrorType;

/**
 * 일반적인 시스템 예외 (예: 예상치 못한 널 포인트 예외 등)
 * 로그 레벨: ERROR
 */
public class SystemException extends BaseException {
    public SystemException(ErrorType errorCode) {
        super(errorCode);
    }

    public SystemException(ErrorType errorCode, Object details) {
        super(errorCode, details);
    }
}
