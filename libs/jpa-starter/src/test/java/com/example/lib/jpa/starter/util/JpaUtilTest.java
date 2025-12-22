package com.example.lib.jpa.starter.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JpaUtilTest {

    static class TestEntity {
        private Long id;
        private String name;
    }

    static class SubEntity extends TestEntity {
        private String description;
    }

    @Test
    @DisplayName("필드 존재 여부 확인 테스트 (본인 필드, 부모 필드, 없는 필드)")
    void checkFieldExistsTest() {
        assertThat(JpaUtil.checkFieldExists(SubEntity.class, "description")).isTrue();
        assertThat(JpaUtil.checkFieldExists(SubEntity.class, "name")).isTrue();
        assertThat(JpaUtil.checkFieldExists(SubEntity.class, "id")).isTrue();
        assertThat(JpaUtil.checkFieldExists(SubEntity.class, "invalid")).isFalse();
    }

    @Test
    @DisplayName("Pageable 유효성 검사 테스트 - 유효한 경우")
    void validatePageableSuccessTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Pageable result = JpaUtil.validatePageable(pageable, TestEntity.class);
        assertThat(result).isEqualTo(pageable);
    }

    @Test
    @DisplayName("Pageable 유효성 검사 테스트 - 유효하지 않은 필드인 경우 예외 발생")
    void validatePageableFailTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("invalid"));
        assertThatThrownBy(() -> JpaUtil.validatePageable(pageable, TestEntity.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No property 'invalid'");
    }
}
