package com.example.springboot_app.global.exception.types;

import com.example.springboot_app.global.exception.enums.ErrorType;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorType errorType;
    private final Object details;

    public BaseException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.details = null;
    }

    public BaseException(ErrorType errorType, Object details) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.details = details;
    }
}
