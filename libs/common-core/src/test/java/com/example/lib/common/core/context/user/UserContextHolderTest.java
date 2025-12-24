package com.example.lib.common.core.context.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserContextHolder 테스트")
class UserContextHolderTest {

    @AfterEach
    void tearDown() {
        UserContextHolder.clearContext();
    }

    @Nested
    @DisplayName("getContext()")
    class GetContext {

        @Test
        @DisplayName("컨텍스트가 없으면 guest 컨텍스트를 반환한다")
        void returnsGuestWhenNoContextSet() {
            CurrentUser user = UserContextHolder.getContext();

            assertThat(user).isNotNull();
            assertThat(user.isGuest()).isTrue();
            assertThat(user.userId()).isEqualTo(-1L);
            assertThat(user.name()).isEqualTo("Guest");
        }

        @Test
        @DisplayName("설정된 컨텍스트를 반환한다")
        void returnsSetContext() {
            UserContext userContext = new UserContext(42L, "Alice", "token-abc",
                    Set.of("ROLE_USER"), Collections.emptyMap());
            UserContextHolder.setContext(userContext);

            CurrentUser result = UserContextHolder.getContext();

            assertThat(result.userId()).isEqualTo(42L);
            assertThat(result.name()).isEqualTo("Alice");
            assertThat(result.token()).isEqualTo("token-abc");
            assertThat(result.roles()).containsExactly("ROLE_USER");
            assertThat(result.isGuest()).isFalse();
        }
    }

    @Nested
    @DisplayName("clearContext()")
    class ClearContext {

        @Test
        @DisplayName("컨텍스트를 지우면 이후 호출 시 guest를 반환한다")
        void returnsGuestAfterClear() {
            UserContext userContext = new UserContext(1L, "Bob", "token", Set.of(), Map.of());
            UserContextHolder.setContext(userContext);
            UserContextHolder.clearContext();

            assertThat(UserContextHolder.getContext().isGuest()).isTrue();
        }
    }

    @Nested
    @DisplayName("스레드 독립성")
    class ThreadIsolation {

        @Test
        @DisplayName("다른 스레드의 컨텍스트는 공유되지 않는다")
        void contextIsThreadLocal() throws InterruptedException {
            UserContext mainContext = new UserContext(1L, "Main", "t1", Set.of(), Map.of());
            UserContextHolder.setContext(mainContext);

            CurrentUser[] threadResult = new CurrentUser[1];
            Thread otherThread = new Thread(() -> {
                threadResult[0] = UserContextHolder.getContext();
            });
            otherThread.start();
            otherThread.join();

            // 다른 스레드에서는 guest가 반환되어야 한다
            assertThat(threadResult[0].isGuest()).isTrue();
            // 메인 스레드의 컨텍스트는 그대로여야 한다
            assertThat(UserContextHolder.getContext().userId()).isEqualTo(1L);
        }
    }
}
