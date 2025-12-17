package com.example.springboot_app.global.exception.handler;

import org.springframework.http.ResponseEntity;

import com.example.springboot_app.global.exception.enums.ErrorType;
import com.example.springboot_app.global.response.types.ApiError;

public abstract class BaseExceptionHandler {
  
    /**
     * 공통 에러 응답 생성
     */
    protected ResponseEntity<ApiError> responseError(ErrorType errorType, String instance, Object details) {
        ApiError apiError = ApiError.of(errorType, instance, details);
        return ResponseEntity.status(errorType.getStatus()).body(apiError);
    }

}
