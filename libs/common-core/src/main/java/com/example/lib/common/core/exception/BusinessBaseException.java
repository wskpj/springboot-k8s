package com.example.lib.common.core.exception;

public abstract class BusinessBaseException extends BaseException {
    public BusinessBaseException(ErrorType errorType) {
        super(errorType);
    }
    public BusinessBaseException(ErrorType errorType, Object details) {
        super(errorType, details);
    }
}