package com.example.lib.event.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DefaultEventType 테스트")
class DefaultEventTypeTest {

    @Test
    @DisplayName("of()로 생성된 인스턴스의 code와 description이 정확히 설정된다")
    void createsWithCodeAndDescription() {
        EventType type = DefaultEventType.of("JPA_001", "낙관적 락 충돌");

        assertThat(type.getCode()).isEqualTo("JPA_001");
        assertThat(type.getDescription()).isEqualTo("낙관적 락 충돌");
    }

    @Test
    @DisplayName("동일한 code/description으로 생성한 두 인스턴스는 다른 객체이다 (값 동등성 없음)")
    void twoInstancesAreNotSame() {
        EventType t1 = DefaultEventType.of("CODE", "Desc");
        EventType t2 = DefaultEventType.of("CODE", "Desc");

        assertThat(t1).isNotSameAs(t2);
    }
}
