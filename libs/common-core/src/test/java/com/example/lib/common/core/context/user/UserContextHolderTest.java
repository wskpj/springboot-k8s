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

    // 테스트용 CurrentUser 구현체
    record TestUser(Long userId, String name, String token, Set<String> roles, Map<String, Object> attributes, boolean isGuest) implements CurrentUser {}

    @Nested
    @DisplayName("getContext()")
    class GetContext {

        @Test
        @DisplayName("컨텍스트가 없으면 null을 반환한다")
        void returnsNullWhenNoContextSet() {
            UserContext context = UserContextHolder.getContext();
            assertThat(context).isNull();
        }

        @Test
        @DisplayName("설정된 컨텍스트를 반환한다")
        void returnsSetContext() {
            CurrentUser data = new TestUser(42L, "Alice", "token-abc", Set.of("ROLE_USER"), Collections.emptyMap(), false);
            UserContext context = new UserContext(data);
            UserContextHolder.setContext(context);

            UserContext result = UserContextHolder.getContext();

            assertThat(result.user().userId()).isEqualTo(42L);
            assertThat(result.user().name()).isEqualTo("Alice");
            assertThat(result.user().token()).isEqualTo("token-abc");
            assertThat(result.user().roles()).containsExactly("ROLE_USER");
            assertThat(result.user().isGuest()).isFalse();
        }
    }

    @Nested
    @DisplayName("clearContext()")
    class ClearContext {

        @Test
        @DisplayName("컨텍스트를 지우면 이후 호출 시 null을 반환한다")
        void returnsNullAfterClear() {
            CurrentUser data = new TestUser(1L, "Bob", "token", Set.of(), Map.of(), false);
            UserContextHolder.setContext(new UserContext(data));
            UserContextHolder.clearContext();

            assertThat(UserContextHolder.getContext()).isNull();
        }
    }

    @Nested
    @DisplayName("스레드 독립성")
    class ThreadIsolation {

        @Test
        @DisplayName("다른 스레드의 컨텍스트는 공유되지 않는다")
        void contextIsThreadLocal() throws InterruptedException {
            CurrentUser mainData = new TestUser(1L, "Main", "t1", Set.of(), Map.of(), false);
            UserContextHolder.setContext(new UserContext(mainData));

            UserContext[] threadResult = new UserContext[1];
            Thread otherThread = new Thread(() -> {
                threadResult[0] = UserContextHolder.getContext();
            });
            otherThread.start();
            otherThread.join();

            // 다른 스레드에서는 null이 반환되어야 한다
            assertThat(threadResult[0]).isNull();
            // 메인 스레드의 컨텍스트는 그대로여야 한다
            assertThat(UserContextHolder.getContext().user().userId()).isEqualTo(1L);
        }
    }
}
