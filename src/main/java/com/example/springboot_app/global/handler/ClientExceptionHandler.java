package com.example.springboot_app.global.handler;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.springboot_app.api.common.dto.ApiResult;
import com.example.springboot_app.global.enums.ErrorType;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@RestControllerAdvice
public class ClientExceptionHandler extends BaseExceptionHandler {

    /**
     * @Valid 어노테이션으로 인한 유효성 검사 실패 시 발생하는 예외 처리 (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ApiResult<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, 
            HttpServletRequest request) {
        log.info("Validation Error: {}", e.getMessage());

        // 에러 목록에서 필요한 정보 추출
        List<FieldErrorDetail> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        return responseError(ErrorType.INVALID_INPUT_VALUE, request.getRequestURI(), details);
    }

    /**
     * 잘못된 인자 전달 시 발생하는 예외 처리 (400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ApiResult<Void>> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.info("Illegal Argument Exception: {}", e.getMessage());
        return responseError(ErrorType.INVALID_INPUT_VALUE, request.getRequestURI(), e.getMessage());
    }

    /**
     * 잘못된 프로퍼티 참조 시 발생하는 예외 처리 (400)
     */
    @ExceptionHandler(PropertyReferenceException.class)
    private ResponseEntity<ApiResult<Void>> handlePropertyReferenceException(PropertyReferenceException e, HttpServletRequest request) {
        log.info("Property Reference Exception: {}", e.getMessage());
        return responseError(ErrorType.INVALID_INPUT_VALUE, request.getRequestURI(), e.getMessage());
    }
}
