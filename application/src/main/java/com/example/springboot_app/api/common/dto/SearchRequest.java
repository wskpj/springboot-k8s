package com.example.springboot_app.api.common.dto;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.lib.jpa.core.dto.SearchParam;
import com.example.lib.jpa.core.enums.SearchType;

import io.swagger.v3.oas.annotations.media.Schema;

public record SearchRequest(
    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    Integer page,

    @Schema(description = "페이지 크기 (기본 10)", example = "10")
    Integer size,

    @Schema(description = "정렬 기준 필드", example = "createdAt")
    String sortBy,

    @Schema(description = "정렬 방향 (ASC, DESC)", example = "DESC")
    String direction,
    
    @Schema(description = "검색 쿼리", example = "example")
    String q,

    @Schema(description = "검색 대상 필드 목록", example = "[\"name\", \"email\"]")
    List<String> fields,

    @Schema(description = "검색 방식 (contains | equals | starts | ends)", example = "contains")
    String type,

    @Schema(description = "시작 일시 (ISO_DATE_TIME)", example = "2020-01-01T00:00:00")
    String dateFrom,

    @Schema(description = "종료 일시 (ISO_DATE_TIME)", example = "2029-12-31T23:59:59")
    String dateTo
) {
    private static final int PAGE_DEFAULT_SIZE = 10;
    private static final int PAGE_MAX_SIZE = 100;

    /**
     * Compact Constructor: 기본값 설정 및 유효성 검증
     */
    public SearchRequest {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = PAGE_DEFAULT_SIZE;
        if (size > PAGE_MAX_SIZE) size = PAGE_MAX_SIZE;

        if (sortBy == null || sortBy.isBlank()) sortBy = "id";
        direction = "ASC".equalsIgnoreCase(direction) ? "ASC" : "DESC";

        if (fields != null) {
            fields = fields.stream()
                    .map(f -> f.replaceAll("[\\[\\]\"']", "").trim())
                    .filter(f -> !f.isBlank())
                    .toList();
        }

        if (type == null) type = "contains";
    }

    /**
     * 도메인 레이어용 Param으로 변환
     */
    public SearchParam toParam() {
        Sort.Direction dir = "ASC".equalsIgnoreCase(direction)
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        
        return new SearchParam(pageable, q, fields, SearchType.from(type), dateFrom, dateTo);
    }
}
