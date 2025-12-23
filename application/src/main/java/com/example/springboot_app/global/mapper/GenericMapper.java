package com.example.springboot_app.global.mapper;

import java.util.function.Function;

import org.springframework.data.domain.Page;

import com.example.lib.jpa.core.dto.Paged;

/**
 * 모든 매퍼에서 공통으로 사용할 수 있는 기본 매핑 로직을 정의합니다.
 */
public interface GenericMapper {

    /**
     * Spring Data JPA의 Page 객체를 커스텀 Paged DTO로 변환합니다.
     */
    default <E, D> Paged<D> toPaged(Page<E> page, Function<E, D> mapper) {
        return Paged.from(page, mapper);
    }
}
