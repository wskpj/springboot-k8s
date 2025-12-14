package com.example.springboot_app.global.error.exception;

import com.example.springboot_app.global.enums.ErrorType;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorType errorCode;
    private final Object details;

    public BaseException(ErrorType errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public BaseException(ErrorType errorCode, Object details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }
}
