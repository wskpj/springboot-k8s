package com.example.lib.jpa.core.enums;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

/**
 * 검색 대상 타입을 정의하는 열거형입니다.
 * JPA 검색 파라미터와 함께 사용됩니다.
 */
@RequiredArgsConstructor
public enum SearchType {
    CONTAINS("contains"),
    STARTS_WITH("startsWith"),
    ENDS_WITH("endsWith"),
    EQUALS("equals");

    private final String value;

    /**
     * 검색 키워드를 바탕으로 DB에 전달할 LIKE 패턴을 생성합니다.
     */
    public String toPattern(String query) {
        return switch (this) {
            case STARTS_WITH -> query + "%";
            case ENDS_WITH -> "%" + query;
            case EQUALS -> query;
            default -> "%" + query + "%";
        };
    }

    /**
     * 문자열로부터 Enum을 매칭합니다. (대소문자 무시, 기본값 CONTAINS)
     */
    public static SearchType from(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(CONTAINS);
    }
}