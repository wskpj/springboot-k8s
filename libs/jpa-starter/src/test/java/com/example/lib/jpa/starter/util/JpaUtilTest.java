package com.example.lib.jpa.starter.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DisplayName("JpaUtil 테스트")
class JpaUtilTest {

    // 테스트용 엔티티 계층
    static class ParentEntity {
        private Long id;
        private String parentField;
    }

    static class ChildEntity extends ParentEntity {
        private String childField;
    }

    @Nested
    @DisplayName("checkFieldExists()")
    class CheckFieldExists {

        @Test
        @DisplayName("자신의 필드는 true를 반환한다")
        void returnsTrueForOwnField() {
            assertThat(JpaUtil.checkFieldExists(ChildEntity.class, "childField")).isTrue();
        }

        @Test
        @DisplayName("부모 클래스 필드까지 재귀 탐색하여 true를 반환한다")
        void returnsTrueForParentField() {
            assertThat(JpaUtil.checkFieldExists(ChildEntity.class, "id")).isTrue();
            assertThat(JpaUtil.checkFieldExists(ChildEntity.class, "parentField")).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 필드는 false를 반환한다")
        void returnsFalseForNonExistentField() {
            assertThat(JpaUtil.checkFieldExists(ChildEntity.class, "nonExistentField")).isFalse();
        }

        @Test
        @DisplayName("null 또는 빈 필드명은 false를 반환한다")
        void returnsFalseForNullOrBlank() {
            assertThat(JpaUtil.checkFieldExists(ChildEntity.class, null)).isFalse();
            assertThat(JpaUtil.checkFieldExists(ChildEntity.class, "")).isFalse();
            assertThat(JpaUtil.checkFieldExists(ChildEntity.class, "   ")).isFalse();
        }
    }

    @Nested
    @DisplayName("validatePageable()")
    class ValidatePageable {

        @Test
        @DisplayName("정렬 없는 Pageable은 그대로 반환된다")
        void returnsUnsortedPageableAsIs() {
            Pageable pageable = PageRequest.of(0, 10);

            Pageable result = JpaUtil.validatePageable(pageable, ChildEntity.class);

            assertThat(result).isSameAs(pageable);
        }

        @Test
        @DisplayName("유효한 자신 필드로 정렬된 Pageable은 그대로 반환된다")
        void acceptsOwnFieldSort() {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("childField"));

            Pageable result = JpaUtil.validatePageable(pageable, ChildEntity.class);

            assertThat(result).isSameAs(pageable);
        }

        @Test
        @DisplayName("부모 클래스 필드로 정렬된 Pageable도 유효하다")
        void acceptsParentFieldSort() {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

            Pageable result = JpaUtil.validatePageable(pageable, ChildEntity.class);

            assertThat(result).isSameAs(pageable);
        }

        @Test
        @DisplayName("존재하지 않는 필드로 정렬 시 IllegalArgumentException이 발생한다")
        void throwsForInvalidSortField() {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("invalidField"));

            assertThatThrownBy(() -> JpaUtil.validatePageable(pageable, ChildEntity.class))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("invalidField")
                    .hasMessageContaining("ChildEntity");
        }

        @Test
        @DisplayName("여러 정렬 중 하나라도 잘못되면 예외가 발생한다")
        void throwsIfAnyFieldInMultiSortIsInvalid() {
            Pageable pageable = PageRequest.of(0, 10,
                    Sort.by("childField").and(Sort.by("invalid")));

            assertThatThrownBy(() -> JpaUtil.validatePageable(pageable, ChildEntity.class))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
