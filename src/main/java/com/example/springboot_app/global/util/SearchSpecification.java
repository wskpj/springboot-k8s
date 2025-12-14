package com.example.springboot_app.global.util;

import com.example.springboot_app.domain.common.dto.SearchParam;
import com.example.springboot_app.domain.common.enums.SearchType;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchSpecification {

    public static <T> Specification<T> build(SearchParam param) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 키워드 검색 (query & fields)
            if (param.q() != null
                && !param.q().isBlank()
                && param.fields() != null
                && !param.fields().isEmpty())
            {
                List<Predicate> orPredicates = new ArrayList<>();
                
                String pattern = param.type().toPattern(param.q());
                for (String field : param.fields()) {
                    // 엔티티에 실제로 존재하는 필드인 경우에만 검색 조건 추가
                    if (isValidField(root, field)) {
                        if (param.type() == SearchType.EQUALS) {
                            orPredicates.add(cb.equal(root.get(field), param.q()));
                        } else {
                            orPredicates.add(cb.like(cb.lower(root.get(field).as(String.class)), pattern.toLowerCase()));
                        }
                    }
                }
                
                if (!orPredicates.isEmpty()) {
                    predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
                }
            }

            // 2. 날짜 범위 검색 (dateFrom, dateTo)
            String dateField = "createdAt"; 
            if (isValidField(root, dateField)) {
                try {
                    if (param.dateFrom() != null && !param.dateFrom().isBlank()) {
                        LocalDateTime start = LocalDateTime.parse(param.dateFrom(), DateTimeFormatter.ISO_DATE_TIME);
                        predicates.add(cb.greaterThanOrEqualTo(root.get(dateField), start));
                    }
                    if (param.dateTo() != null && !param.dateTo().isBlank()) {
                        LocalDateTime end = LocalDateTime.parse(param.dateTo(), DateTimeFormatter.ISO_DATE_TIME);
                        predicates.add(cb.lessThanOrEqualTo(root.get(dateField), end));
                    }
                } catch (Exception e) {
                    // 날짜 형식이 잘못된 경우 해당 필터 무시
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 엔티티(Root) 내에 해당 필드명이 실제로 존재하는지 메타데이터를 통해 확인합니다.
     */
    private static boolean isValidField(Root<?> root, String fieldName) {
        if (fieldName == null || fieldName.isBlank()) return false;
        return root.getModel().getAttributes().stream()
                .anyMatch(attr -> attr.getName().equals(fieldName));
    }
}