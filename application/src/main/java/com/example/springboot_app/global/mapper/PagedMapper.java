package com.example.springboot_app.global.mapper;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.lib.jpa.core.dto.Paged;

/**
 * Spring Data Page 객체를 커스텀 Paged DTO로 변환하는 공통 기능을 제공합니다.
 */
public interface PagedMapper {

    /**
     * Page 객체의 메타데이터를 Paged DTO로 매핑합니다.
     * 컨텐츠 변환은 각 도메인 매퍼의 구현에 위임합니다.
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
