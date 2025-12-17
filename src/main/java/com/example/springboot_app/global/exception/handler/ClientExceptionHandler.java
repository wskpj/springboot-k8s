package com.example.springboot_app.global.exception.handler;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.response.types.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@RestControllerAdvice
public class ClientExceptionHandler extends BaseExceptionHandler {

    private record FieldErrorDetail(String field, String message) {}

    /**
     * @Valid 어노테이션으로 인한 유효성 검사 실패 시 발생하는 예외 처리 (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ApiError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, 
            HttpServletRequest request) {
        log.info("[Exception] Validation Error: {}", e.getMessage());

        // 에러 목록에서 필요한 정보 추출
        List<FieldErrorDetail> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        return responseError(GlobalError.BAD_REQUEST, request.getRequestURI(), details);
    }

    /**
     * 잘못된 인자 전달 시 발생하는 예외 처리 (400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.info("[Exception] Illegal Argument Exception: {}", e.getMessage());
        return responseError(GlobalError.BAD_REQUEST, request.getRequestURI(), e.getMessage());
    }

    /**
     * 잘못된 프로퍼티 참조 시 발생하는 예외 처리 (400)
     */
    @ExceptionHandler(PropertyReferenceException.class)
    private ResponseEntity<ApiError> handlePropertyReferenceException(PropertyReferenceException e, HttpServletRequest request) {
        log.info("[Exception] Property Reference Exception: {}", e.getMessage());
        return responseError(GlobalError.BAD_REQUEST, request.getRequestURI(), e.getMessage());
    }
}
