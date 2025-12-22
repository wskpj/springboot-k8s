package com.example.lib.web.starter.handler;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.lib.web.starter.bean.ApiGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * 모든 REST 컨트롤러의 응답을 가로채어 ApiResult 규격으로 래핑하는 추상 클래스입니다.
 * 이 라이브러리를 사용하는 애플리케이션은 이 클래스를 상속받아 
 * @RestControllerAdvice(basePackages = "...") 를 선언하여 사용합니다.
 */
@RequiredArgsConstructor
public abstract class BaseResponseHandler implements ResponseBodyAdvice<Object> {

    protected final ApiGenerator apiGenerator;    
    protected final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        // 파일(Resource, byte[]) 다운로드는 래핑에서 제외
        Class<?> parameterType = returnType.getParameterType();
        if (Resource.class.isAssignableFrom(parameterType) || 
            byte[].class.isAssignableFrom(parameterType)){
            return false;
        }

        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType,
                                  Class<? extends HttpMessageConverter<?>> converterType, 
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // ApiResult 규격으로 래핑
        Object wrappedBody = apiGenerator.generate(body, response);

        // 반환 타입이 String인 경우, StringHttpMessageConverter 사용을 위한 전처리(직렬화) 수행
        if (body instanceof String || returnType.getParameterType().equals(String.class)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON); // ContentType 설정 필수
            return objectMapper.writeValueAsString(wrappedBody); // 직렬화된 JSON 문자열 반환
        }

        return wrappedBody;
    }
}
