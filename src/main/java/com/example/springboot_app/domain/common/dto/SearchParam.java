package com.example.springboot_app.domain.common.dto;

import com.example.springboot_app.domain.common.enums.SearchType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public record SearchParam(
    Pageable pageable,
    String q,
    List<String> fields,
    SearchType type,
    Map<String, Object> filters,
    String dateFrom,
    String dateTo
) {}
