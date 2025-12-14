package com.example.springboot_app.domain.common.dto;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

public record SearchParam(
    Pageable pageable,
    String q,
    List<String> fields,
    String type,
    Map<String, Object> filters,
    String dateFrom,
    String dateTo
) {}
