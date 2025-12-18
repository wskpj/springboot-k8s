package com.example.springboot_app.global.exception.handler;

import com.example.springboot_app.global.exception.enums.ErrorType;
import com.example.springboot_app.global.response.types.ApiError;

public abstract class BaseExceptionHandler {
  
    protected ApiError errorInstance(ErrorType errorType, String instance, Object details) {
        return ApiError.of(errorType, instance, details);
    }

}
