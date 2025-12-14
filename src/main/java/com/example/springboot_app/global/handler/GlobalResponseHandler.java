package com.example.springboot_app.global.handler;

import com.example.springboot_app.api.common.dto.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.example.springboot_app.api")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 모든 응답에 대해 동작하되, 구체적인 조건은 basePackages에서 필터링
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // 이미 ApiResult 형태인 경우 재래핑 방지
        if (body instanceof ApiResult) return body;

        // ApiResult.success로 래핑
        ApiResult<Object> result = ApiResult.success(body);

        // StringHttpMessageConverter는 String 외의 객체를 처리하지 못함
        // 반환 타입이 String인 경우 JSON 문자열로 직접 변환하여 반환
        if (selectedConverterType.getName().contains("StringHttpMessageConverter")) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                log.error("Failed to convert ApiResult to JSON string", e);
                return result; // Fallback Action
            }
        }

        return result;
    }
}
