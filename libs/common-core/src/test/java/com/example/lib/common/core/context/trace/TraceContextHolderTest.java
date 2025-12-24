package com.example.lib.common.core.context.trace;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TraceContextHolder 테스트")
class TraceContextHolderTest {

    @AfterEach
    void tearDown() {
        TraceContextHolder.clearContext();
    }

    // 테스트용 CurrentTrace 구현체
    record TestTrace(String traceId, String spanId, String parentSpanId, boolean sampled, OffsetDateTime startTime) implements CurrentTrace {}

    @Nested
    @DisplayName("getTraceId()")
    class GetTraceId {

        @Test
        @DisplayName("컨텍스트가 없으면 null을 반환한다")
        void returnsNullWhenNoContextSet() {
            assertThat(TraceContextHolder.getTraceId()).isNull();
        }

        @Test
        @DisplayName("설정된 traceId를 반환한다")
        void returnsSetTraceId() {
            String traceId = "trace-123";
            CurrentUser data = new TestTrace(traceId, "span", null, true, OffsetDateTime.now());
            TraceContextHolder.setContext(new TraceContext((CurrentTrace) data));

            assertThat(TraceContextHolder.getTraceId()).isEqualTo(traceId);
        }
    }

    @Nested
    @DisplayName("getContext()")
    class GetContext {

        @Test
        @DisplayName("컨텍스트가 없으면 null을 반환한다")
        void returnsNullWhenNoContext() {
            assertThat(TraceContextHolder.getContext()).isNull();
        }

        @Test
        @DisplayName("설정된 TraceContext를 반환한다")
        void returnsContextAsTraceContext() {
            CurrentTrace data = new TestTrace("abc", "s1", null, true, OffsetDateTime.now());
            TraceContext ctx = new TraceContext(data);
            TraceContextHolder.setContext(ctx);

            TraceContext result = TraceContextHolder.getContext();

            assertThat(result).isNotNull();
            assertThat(result.trace().traceId()).isEqualTo("abc");
            assertThat(result.trace().startTime()).isNotNull();
        }
    }

    @Nested
    @DisplayName("스레드 독립성")
    class ThreadIsolation {

        @Test
        @DisplayName("다른 스레드의 traceId는 공유되지 않는다")
        void traceIdIsThreadLocal() throws InterruptedException {
            CurrentTrace data = new TestTrace("main-trace", "s1", null, true, OffsetDateTime.now());
            TraceContextHolder.setContext(new TraceContext(data));

            String[] threadTraceId = new String[1];
            Thread other = new Thread(() -> {
                threadTraceId[0] = TraceContextHolder.getTraceId();
            });
            other.start();
            other.join();

            assertThat(threadTraceId[0]).isNull();
            assertThat(TraceContextHolder.getTraceId()).isEqualTo("main-trace");
        }
    }
}
