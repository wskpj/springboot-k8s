package com.example.springboot_app.global.exception.types.base;

import com.example.springboot_app.global.exception.enums.ErrorType;

import lombok.Getter;

/**
 * 사용자 정의 예외
 */
@Getter
public abstract class BaseException extends RuntimeException {

    private final ErrorType errorType;
    private final Object details;

    protected BaseException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.details = null;
    }

    protected BaseException(ErrorType errorType, Object details) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.details = details;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; // BaseException을 비롯한 Handled Exception의 경우, 스택 트레이스를 채우지 않고 바로 반환
    }

}
