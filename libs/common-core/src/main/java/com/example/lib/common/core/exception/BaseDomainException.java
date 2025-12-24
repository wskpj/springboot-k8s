package com.example.lib.common.core.exception;

public abstract class BaseDomainException extends BaseException {
    public BaseDomainException(ErrorType errorType) {
        super(errorType);
    }
    public BaseDomainException(ErrorType errorType, Object details) {
        super(errorType, details);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
