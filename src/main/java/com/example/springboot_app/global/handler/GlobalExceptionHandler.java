package com.example.springboot_app.global.handler;

import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.error.exception.InfrastructureException;
import com.example.springboot_app.global.error.exception.SystemException;
import com.example.springboot_app.api.common.dto.ApiResult;
import com.example.springboot_app.api.common.dto.ErrorResponse;
import com.example.springboot_app.global.error.ErrorType;
import com.example.springboot_app.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResult<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("Validation Error: {}", e.getMessage());
        return createErrorResponse(ErrorType.INVALID_INPUT_VALUE, request, null);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResult<Void>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("Business Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return createErrorResponse(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(InfrastructureException.class)
    protected ResponseEntity<ApiResult<Void>> handleInfrastructureException(InfrastructureException e, HttpServletRequest request) {
        log.error("Infrastructure Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return createErrorResponse(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ApiResult<Void>> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("Base Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return createErrorResponse(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(SystemException.class)
    protected ResponseEntity<ApiResult<Void>> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("System Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return createErrorResponse(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResult<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("Unhandled Exception: ", e);
        return createErrorResponse(ErrorType.INTERNAL_SERVER_ERROR, request, e.getMessage());
    }

    private ResponseEntity<ApiResult<Void>> createErrorResponse(ErrorType errorCode, HttpServletRequest request, Object details) {
        String requestId = Optional.ofNullable((String) request.getAttribute("requestId")).orElse(UUID.randomUUID().toString());

        ErrorResponse httpError = ErrorResponse.of(errorCode, request.getRequestURI(), details);
        return ResponseEntity.status(errorCode.getStatus()).body(ApiResult.fail(httpError, requestId));
    }
}
