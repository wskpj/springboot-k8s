package com.example.lib.common.core.exception;

public abstract class BaseSystemException extends BaseException {
    public BaseSystemException(ErrorType errorType) {
        super(errorType);
    }
    public BaseSystemException(ErrorType errorType, Object details) {
        super(errorType, details);
    }
}
