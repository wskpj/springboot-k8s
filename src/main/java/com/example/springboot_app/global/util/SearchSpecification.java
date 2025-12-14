package com.example.springboot_app.global.util;

import com.example.springboot_app.domain.common.dto.SearchParam;
import jakarta.persistence.criteria.Predicate;
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
                
                String pattern = switch (param.type()) {
                    case "startsWith" -> param.q() + "%";
                    case "endsWith" -> "%" + param.q();
                    case "equals" -> param.q();
                    default -> "%" + param.q() + "%";
                };

                for (String field : param.fields()) {
                    if ("equals".equals(param.type())) {
                        orPredicates.add(cb.equal(root.get(field), param.q()));
                    } else {
                        orPredicates.add(cb.like(cb.lower(root.get(field).as(String.class)), pattern.toLowerCase()));
                    }
                }
                predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
            }

            // 2. 날짜 범위 검색 (dateFrom, dateTo)
            String dateField = "createdAt"; 
            if (param.dateFrom() != null && !param.dateFrom().isBlank()) {
                LocalDateTime start = LocalDateTime.parse(param.dateFrom(), DateTimeFormatter.ISO_DATE_TIME);
                predicates.add(cb.greaterThanOrEqualTo(root.get(dateField), start));
            }
            if (param.dateTo() != null && !param.dateTo().isBlank()) {
                LocalDateTime end = LocalDateTime.parse(param.dateTo(), DateTimeFormatter.ISO_DATE_TIME);
                predicates.add(cb.lessThanOrEqualTo(root.get(dateField), end));
            }

            // 3. 추가 필터 (filters)
            if (param.filters() != null) {
                param.filters().forEach((key, value) -> {
                    if (value != null) {
                        predicates.add(cb.equal(root.get(key), value));
                    }
                });
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}