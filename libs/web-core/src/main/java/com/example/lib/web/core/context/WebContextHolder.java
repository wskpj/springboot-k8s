package com.example.lib.web.core.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * 현재 HTTP 요청 및 응답에 대한 컨텍스트를 제공하는 유틸리티 클래스입니다.
 * 스레드 로컬에 저장된 요청 정보를 편리하게 추출합니다.
 */
public class WebContextHolder {

    private WebContextHolder() {
    }

    /**
     * 현재 요청 스레드의 HttpServletRequest를 반환합니다.
     */
    public static Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }

    /**
     * 현재 요청 스레드의 HttpServletResponse를 반환합니다.
     */
    public static Optional<HttpServletResponse> getResponse() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getResponse);
    }

    /**
     * 현재 요청 스레드의 HttpServletResponse를 반환하며, 없을 경우 예외를 발생시킵니다.
     */
    public static HttpServletResponse getRequiredResponse() {
        return getResponse().orElseThrow(() -> new IllegalStateException("HTTP Response is not available in current thread."));
    }

    /**
     * 현재 요청 스레드의 HttpServletRequest를 반환하며, 없을 경우 예외를 발생시킵니다.
     */
    public static HttpServletRequest getRequiredRequest() {
        return getRequest().orElseThrow(() -> new IllegalStateException("HTTP Request is not available in current thread."));
    }
}
