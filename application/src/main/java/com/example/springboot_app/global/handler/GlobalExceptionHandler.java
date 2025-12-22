package com.example.springboot_app.global.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.starter.handler.BaseExceptionHandler;

/**
 * 전역 예외 처리를 담당하는 애플리케이션 어드바이스입니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

    public GlobalExceptionHandler(ErrorDispatcher dispatcher) {
        super(dispatcher);
    }
}
