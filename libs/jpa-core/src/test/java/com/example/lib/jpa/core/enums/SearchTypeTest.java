package com.example.lib.jpa.core.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SearchType 테스트")
class SearchTypeTest {

    @Nested
    @DisplayName("toPattern()")
    class ToPattern {

        @ParameterizedTest(name = "{0}.toPattern(\"{1}\") = \"{2}\"")
        @CsvSource({
            "CONTAINS,    keyword, %keyword%",
            "STARTS_WITH, keyword, keyword%",
            "ENDS_WITH,   keyword, %keyword",
            "EQUALS,      keyword, keyword"
        })
        @DisplayName("검색 타입에 따라 올바른 LIKE 패턴을 생성한다")
        void generatesCorrectPattern(SearchType type, String query, String expected) {
            assertThat(type.toPattern(query)).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("from()")
    class From {

        @ParameterizedTest(name = "from(\"{0}\") = {1}")
        @CsvSource({
            "contains,   CONTAINS",
            "startsWith, STARTS_WITH",
            "endsWith,   ENDS_WITH",
            "equals,     EQUALS",
            "invalid,    CONTAINS",
            ",           CONTAINS"
        })
        @DisplayName("문자열에서 SearchType을 매핑하고, 없으면 CONTAINS 기본값을 반환한다")
        void mapsFromString(String value, SearchType expected) {
            assertThat(SearchType.from(value)).isEqualTo(expected);
        }

        @Test
        @DisplayName("대소문자를 무시하고 매칭한다")
        void isCaseInsensitive() {
            assertThat(SearchType.from("CONTAINS")).isEqualTo(SearchType.CONTAINS);
            assertThat(SearchType.from("StartsWith")).isEqualTo(SearchType.STARTS_WITH);
        }

        @Test
        @DisplayName("null 입력 시 CONTAINS(기본값)를 반환한다")
        void returnsContainsForNull() {
            assertThat(SearchType.from(null)).isEqualTo(SearchType.CONTAINS);
        }
    }
}
