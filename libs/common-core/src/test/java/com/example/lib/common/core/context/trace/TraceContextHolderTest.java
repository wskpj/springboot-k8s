package com.example.lib.common.core.context.trace;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TraceContextHolder 테스트")
class TraceContextHolderTest {

    @AfterEach
    void tearDown() {
        TraceContextHolder.clearContext();
    }

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
            TraceContextHolder.setContext(TraceContext.create(traceId));

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
        @DisplayName("설정된 TraceContext를 CurrentTrace로 반환한다")
        void returnsContextAsCurrentTrace() {
            TraceContext ctx = TraceContext.create("abc");
            TraceContextHolder.setContext(ctx);

            CurrentTrace result = TraceContextHolder.getContext();

            assertThat(result).isNotNull();
            assertThat(result.traceId()).isEqualTo("abc");
            assertThat(result.startTime()).isNotNull();
        }
    }

    @Nested
    @DisplayName("스레드 독립성")
    class ThreadIsolation {

        @Test
        @DisplayName("다른 스레드의 traceId는 공유되지 않는다")
        void traceIdIsThreadLocal() throws InterruptedException {
            TraceContextHolder.setContext(TraceContext.create("main-trace"));

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
