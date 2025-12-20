package com.example.springboot_app.global.exception.dispatcher;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.exception.strategy.error.DefaultExceptionStrategy;
import com.example.springboot_app.global.response.types.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorResponseDispatcher {

    private final List<ExceptionHandleStrategy<?>> strategies;
    private final DefaultExceptionStrategy fallback;

    @SuppressWarnings("unchecked")
    public ApiError dispatch(Exception e, HttpServletRequest request) {
        String uri = request.getRequestURI();

        ExceptionHandleStrategy<Exception> strategy = (ExceptionHandleStrategy<Exception>) strategies.stream()
                .filter(s -> !(s instanceof DefaultExceptionStrategy))
                .filter(s -> s.supports(e))
                .findFirst()
                .orElse((ExceptionHandleStrategy) fallback);

        return strategy.handle(e, uri);
    }
}
