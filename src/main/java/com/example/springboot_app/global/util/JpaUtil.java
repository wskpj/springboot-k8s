package com.example.springboot_app.global.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class JpaUtil {

    /**
     * Pageable의 정렬 필드가 유효한지 검사하고, 유효하지 않으면 기본값(id)으로 대체된 새로운 Pageable을 반환합니다.
     */
    public static Pageable validatePageable(Pageable pageable, Class<?> entityClass) {
        if (pageable.getSort().isUnsorted()) {
            return pageable;
        }

        List<Sort.Order> validOrders = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            if (checkFieldExists(entityClass, order.getProperty())) {
                validOrders.add(order);
            }
        }

        // 유효한 정렬 조건이 하나도 없으면 id DESC로 기본 설정
        if (validOrders.isEmpty()) {
            return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
            );
        }

        return PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            Sort.by(validOrders)
        );
    }

    /**
     * 엔티티 클래스 내에 특정 필드가 존재하는지 리플렉션으로 확인합니다.
     * 부모 클래스(BaseEntity 등)의 필드까지 재귀적으로 검사합니다.
     */
    public static boolean checkFieldExists(Class<?> entityClass, String propertyName) {
        if (propertyName == null || propertyName.isBlank()) return false;

        Class<?> current = entityClass;
        while (current != null && current != Object.class) {
            try {
                current.getDeclaredField(propertyName);
                return true;
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return false;
    }
}
