package com.example.lib.jpa.core.dto;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.lib.jpa.core.enums.SearchType;

/**
 * 페이징 및 검색 조건을 담는 공통 파라미터 객체입니다.
 */
public record SearchParam(
        Pageable pageable,
        String q,
        List<String> fields,
        SearchType type,
        String dateFrom,
        String dateTo) {

    public Pageable pageable() {
        return pageable != null ? pageable : PageRequest.of(0, 10);
    }
}
