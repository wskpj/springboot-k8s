package com.example.springboot_app.global.error;

import com.example.springboot_app.common.dto.ApiResult;
import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.error.exception.InfrastructureException;
import com.example.springboot_app.global.error.exception.SystemException;
import com.example.springboot_app.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResult<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("Validation Error: {}", e.getMessage());
        return createErrorResponse(ErrorCode.INVALID_INPUT_VALUE, request, null);
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

    @ExceptionHandler(SystemException.class)
    protected ResponseEntity<ApiResult<Void>> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("System Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return createErrorResponse(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ApiResult<Void>> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("Base Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return createErrorResponse(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResult<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("Unhandled Exception: ", e);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, request, e.getMessage());
    }

    private ResponseEntity<ApiResult<Void>> createErrorResponse(ErrorCode errorCode, HttpServletRequest request, Object details) {
        String requestId = (String) request.getAttribute("requestId");
        if (requestId == null) requestId = UUID.randomUUID().toString();

        HttpError httpError = HttpError.of(errorCode, requestId, request.getRequestURI(), details);
        return ResponseEntity.status(errorCode.getStatus()).body(ApiResult.fail(httpError));
    }
}
