package com.example.springboot_app.domain.common.dto;

import org.springframework.data.domain.Page;
import java.util.List;
import java.util.function.Function;

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
