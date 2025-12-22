package com.example.lib.common.core.exception;

public abstract class SystemBaseException extends BaseException {
    public SystemBaseException(ErrorType errorType) {
        super(errorType);
    }
    public SystemBaseException(ErrorType errorType, Object details) {
        super(errorType, details);
    }
}