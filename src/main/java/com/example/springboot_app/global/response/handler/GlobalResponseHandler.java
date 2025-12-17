package com.example.springboot_app.global.response.handler;

import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.springboot_app.global.response.types.ApiError;
import com.example.springboot_app.global.response.types.ApiResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.example.springboot_app.api")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;  // basePackage 내 모든 응답에 대해 적용
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // 이미 ApiResult 형태인 경우 재래핑 방지
        if (body instanceof ApiResult) return body;

        // RequestId 추출
        String requestId = MDC.get("requestId");

        // [1/2] 에러 응답인 경우 fail로 래핑
        if (body instanceof ApiError apiError) {
            return ApiResult.fail(apiError, requestId);
        }

        // [2/2] 성공 응답인 경우 success로 래핑
        return ApiResult.success(body, requestId);
    }
}
