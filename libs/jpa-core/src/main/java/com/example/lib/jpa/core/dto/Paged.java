package com.example.lib.jpa.core.dto;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

/**
 * 서비스 계층에서 사용하는 공통 페이징 응답 객체입니다.
 * Spring Data JPA의 Page 객체를 의존성 없는 DTO로 변환하여 전달합니다.
 */
public record Paged<T>(
    List<T> content,
    int pageNumber,
    int pageSize,
    int totalPages,
    long totalElements, 
    boolean isFirst,
    boolean isLast
) {
    /**
     * Spring Data JPA의 Page 객체를 Paged DTO로 변환합니다.
     */
    public static <E, D> Paged<D> from(Page<E> page, Function<E, D> mapper) {
        return new Paged<>(
            page.getContent().stream().map(mapper).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.isFirst(),
            page.isLast()
        );
    }
}
