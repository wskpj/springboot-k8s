package com.example.lib.common.core.exception;

import lombok.Getter;

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
        return this;
    }
}