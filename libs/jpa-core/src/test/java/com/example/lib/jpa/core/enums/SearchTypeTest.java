package com.example.lib.jpa.core.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SearchTypeTest {

    @ParameterizedTest
    @CsvSource({
        "CONTAINS, keyword, %keyword%",
        "STARTS_WITH, keyword, keyword%",
        "ENDS_WITH, keyword, %keyword",
        "EQUALS, keyword, keyword"
    })
    @DisplayName("SearchType에 따른 LIKE 패턴 생성이 올바른지 확인한다")
    void toPatternTest(SearchType type, String query, String expected) {
        assertThat(type.toPattern(query)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "contains, CONTAINS",
        "startsWith, STARTS_WITH",
        "endsWith, ENDS_WITH",
        "equals, EQUALS",
        "invalid, CONTAINS",
        ", CONTAINS"
    })
    @DisplayName("문자열로부터 SearchType을 올바르게 매칭하는지 확인한다")
    void fromTest(String value, SearchType expected) {
        assertThat(SearchType.from(value)).isEqualTo(expected);
    }
}
