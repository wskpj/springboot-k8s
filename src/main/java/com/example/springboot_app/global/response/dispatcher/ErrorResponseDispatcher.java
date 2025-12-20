package com.example.springboot_app.global.response.dispatcher;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.exception.strategy.ExceptionStrategy;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErrorResponseDispatcher {

    private final List<ExceptionHandleStrategy<?>> strategies;
    private final ExceptionStrategy fallback;

    @SuppressWarnings("unchecked")
    public ApiError dispatch(Exception e, String path) {
        return strategies.stream()
                .filter(s -> !(s instanceof ExceptionStrategy))
                .filter(s -> s.supports(e))
                .findFirst()
                .map(s -> ((ExceptionHandleStrategy<Exception>) s).handle(e, path))
                .orElseGet(() -> fallback.handle(e, path));
    }
}
