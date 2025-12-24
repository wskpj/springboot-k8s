package com.example.lib.jpa.core.dto;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Spring Data의 Page 객체를 커스텀 Paged DTO로 변환하는 공통 인터페이스입니다.
 * 각 도메인 매퍼에서 implements하여 사용합니다.
 */
public interface PagedMapper {

    /**
     * Page 객체의 메타데이터를 Paged DTO로 매핑합니다.
     * 컨텐츠 변환은 각 도메인 매퍼 구현에 위임합니다.
     */
    default <T> Paged<T> mapPaged(Page<?> page, List<T> content) {
        return new Paged<>(
            content,
            page.getNumber(),
            page.getSize(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.isFirst(),
            page.isLast()
        );
    }
}
