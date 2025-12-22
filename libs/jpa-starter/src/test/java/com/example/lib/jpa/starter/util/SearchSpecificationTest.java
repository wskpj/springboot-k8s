package com.example.lib.jpa.starter.util;

import com.example.lib.jpa.core.dto.SearchParam;
import com.example.lib.jpa.core.enums.SearchType;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SearchSpecificationTest {

    @Test
    @DisplayName("SearchParam을 기반으로 Specification이 올바르게 생성되는지 확인한다")
    @SuppressWarnings("unchecked")
    void buildTest() {
        // given
        SearchParam param = new SearchParam(
                PageRequest.of(0, 10),
                "test",
                List.of("name"),
                SearchType.CONTAINS,
                null,
                null
        );

        Root<Object> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Object> path = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.getJavaType()).thenReturn((Class) Object.class);
        // JpaUtil.checkFieldExists 를 통과시키기 위해 실제 필드가 있는 클래스를 사용하거나 
        // JpaUtil을 Mocking 해야 하는데, JpaUtil은 static이므로 Mockito-inline 필요.
        // 여기선 단순화를 위해 JpaUtil이 checkFieldExists를 리플렉션으로 수행하므로 Object 대신 실제 필드가 있는 클래스 사용.
        when(root.getJavaType()).thenReturn((Class) TestEntity.class);
        when(root.get("name")).thenReturn(path);
        when(path.as(String.class)).thenReturn((Expression) path);
        when(cb.lower(any())).thenReturn((Expression) path);
        when(cb.like(any(), anyString())).thenReturn(predicate);
        when(cb.or(any())).thenReturn(predicate);
        when(cb.and(any())).thenReturn(predicate);

        // when
        Specification<Object> spec = SearchSpecification.build(param);
        Predicate result = spec.toPredicate(root, query, cb);

        // then
        assertThat(result).isNotNull();
        verify(root).get("name");
        verify(cb).like(any(), eq("%test%"));
    }

    static class TestEntity {
        private String name;
        private java.time.LocalDateTime createdAt;
    }
}
