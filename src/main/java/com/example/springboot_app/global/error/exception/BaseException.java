package com.example.springboot_app.global.error.exception;

import com.example.springboot_app.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object details;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public BaseException(ErrorCode errorCode, Object details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }
}
