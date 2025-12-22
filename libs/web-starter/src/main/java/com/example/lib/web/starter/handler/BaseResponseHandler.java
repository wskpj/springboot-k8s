package com.example.lib.web.starter.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.lib.web.starter.bean.ApiGenerator;

import lombok.RequiredArgsConstructor;

/**
 * 모든 REST 컨트롤러의 응답을 가로채어 ApiResult 규격으로 래핑하는 추상 클래스입니다.
 * 이 라이브러리를 사용하는 애플리케이션은 이 클래스를 상속받아 
 * @RestControllerAdvice(basePackages = "...") 를 선언하여 사용합니다.
 */
@RequiredArgsConstructor
public abstract class BaseResponseHandler implements ResponseBodyAdvice<Object> {

    protected final ApiGenerator apiGenerator;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, 
                                  ServerHttpRequest request, ServerHttpResponse response) {
        return apiGenerator.generate(body, response);
    }
}
