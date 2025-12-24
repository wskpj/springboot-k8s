package com.example.lib.jpa.core.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.lib.jpa.core.dto.SearchParam;

import jakarta.persistence.criteria.Predicate;

/**
 * SearchParam을 기반으로 JPA Specification을 동적 생성하는 유틸리티입니다.
 */
public final class SearchSpecification {

    private SearchSpecification() {}

    public static <T> Specification<T> build(SearchParam param) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (param != null && param.q() != null && !param.q().isEmpty()) {
                String pattern = param.type() != null ? param.type().toPattern(param.q()) : "%" + param.q() + "%";
                
                if (param.fields() != null && !param.fields().isEmpty()) {
                    List<Predicate> fieldPredicates = new ArrayList<>();
                    for (String field : param.fields()) {
                        fieldPredicates.add(cb.like(root.get(field), pattern));
                    }
                    predicates.add(cb.or(fieldPredicates.toArray(new Predicate[0])));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}