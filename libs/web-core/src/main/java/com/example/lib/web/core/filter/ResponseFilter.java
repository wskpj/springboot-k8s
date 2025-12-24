package com.example.lib.web.core.filter;

import java.util.Arrays;
import org.springframework.core.MethodParameter;

/**
 * ResponseBodyAdvice의 처리 대상을 제어하는 SPI 인터페이스입니다.
 */
@FunctionalInterface
public interface ResponseFilter {

    /**
     * 해당 컨트롤러 메서드의 응답을 처리할지 여부를 반환합니다.
     */
    boolean supports(MethodParameter returnType);

    /**
     * 지정한 패키지 접두사 중 하나라도 일치하면 처리하는 필터를 생성합니다.
     */
    static ResponseFilter ofPrefixes(String... prefixes) {
        return returnType -> {
            String pkg = returnType.getDeclaringClass().getPackageName();
            return Arrays.stream(prefixes).anyMatch(pkg::startsWith);
        };
    }
}
