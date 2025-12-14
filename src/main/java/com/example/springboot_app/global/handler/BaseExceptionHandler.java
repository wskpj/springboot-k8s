package com.example.springboot_app.global.handler;

import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;

import com.example.springboot_app.api.common.dto.ApiResult;
import com.example.springboot_app.api.common.dto.ErrorResponse;
import com.example.springboot_app.global.enums.ErrorType;

public abstract class BaseExceptionHandler {
  
    /**
     * 공통 에러 응답 생성
     */
    protected ResponseEntity<ApiResult<Void>> responseError(ErrorType errorCode, String instance, Object details) {
        String requestId = MDC.get("requestId");

        ErrorResponse httpError = ErrorResponse.of(errorCode, instance, details);
        return ResponseEntity.status(errorCode.getStatus()).body(ApiResult.fail(httpError, requestId));
    }

    protected record FieldErrorDetail(String field, String message) {}
}
