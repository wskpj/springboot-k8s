package com.example.lib.jpa.core.util;

import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.lib.jpa.core.dto.SearchParam;

/**
 * JPA 관련 공통 유틸리티 클래스입니다.
 */
public final class JpaUtil {

    private JpaUtil() {}

    /**
     * SearchParam을 기반으로 Pageable 객체를 반환합니다.
     */
    public static Pageable createPageable(SearchParam param) {
        if (param == null) {
            return PageRequest.of(0, 10);
        }
        return param.pageable();
    }

    /**
     * Optional을 사용하여 null 안정성을 확보하며 Pageable을 가져옵니다.
     */
    public static Pageable getPageableOrDefault(SearchParam param, int defaultSize) {
        return Optional.ofNullable(param)
                .map(SearchParam::pageable)
                .orElse(PageRequest.of(0, defaultSize));
    }
}
