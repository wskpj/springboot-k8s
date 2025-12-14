package com.example.springboot_app.api.common.dto;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.springboot_app.domain.common.dto.SearchParam;
import com.example.springboot_app.domain.common.enums.SearchType;

public record SearchRequest(
    Integer page,
    Integer size,
    String sortBy,
    String direction,
    
    String q,
    List<String> fields,
    String type,
    Map<String, Object> filters,
    String dateFrom,
    String dateTo
) {
    
    /**
     * Compact Constructor: 기본값 설정 및 유효성 검증
     */
    public SearchRequest {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 10;
        if (sortBy == null || sortBy.isBlank()) sortBy = "id";
        if (direction == null || direction.isBlank()) direction = "DESC";
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
        
        return new SearchParam(pageable, q, fields, SearchType.from(type), filters, dateFrom, dateTo);
    }
}
