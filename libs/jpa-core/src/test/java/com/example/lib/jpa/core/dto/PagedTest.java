package com.example.lib.jpa.core.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PagedTest {

    @Test
    @DisplayName("Spring Data Page 객체로부터 Paged DTO로의 변환이 올바른지 확인한다")
    void fromTest() {
        // given
        List<String> content = List.of("user1", "user2");
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<String> page = new PageImpl<>(content, pageRequest, 2);

        // when
        Paged<String> paged = Paged.from(page, s -> s.toUpperCase());

        // then
        assertThat(paged.content()).containsExactly("USER1", "USER2");
        assertThat(paged.pageNumber()).isEqualTo(0);
        assertThat(paged.pageSize()).isEqualTo(10);
        assertThat(paged.totalElements()).isEqualTo(2);
        assertThat(paged.totalPages()).isEqualTo(1);
        assertThat(paged.isFirst()).isTrue();
        assertThat(paged.isLast()).isTrue();
    }
}
