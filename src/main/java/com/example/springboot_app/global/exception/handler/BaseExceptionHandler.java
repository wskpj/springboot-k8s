package com.example.springboot_app.global.exception.handler;

import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;

import com.example.springboot_app.global.exception.enums.ErrorType;
import com.example.springboot_app.global.response.types.ApiError;
import com.example.springboot_app.global.response.types.ApiResult;

public abstract class BaseExceptionHandler {
  
    /**
     * 공통 에러 응답 생성
     */
    protected ResponseEntity<ApiResult<Void>> responseError(ErrorType errorType, String instance, Object details) {
        String requestId = MDC.get("requestId");

        ApiError apiError = ApiError.of(errorType, instance, details);
        return ResponseEntity.status(errorType.getStatus()).body(ApiResult.fail(apiError, requestId));
    }

    protected record FieldErrorDetail(String field, String message) {}
}
