package com.example.lib.jpa.starter.event;

import com.example.lib.event.core.EventSource;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 애플리케이션에서 주입한 EventSource를 라이브러리 내부에서 참조할 수 있게 보관하는 홀더입니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaEventSourceHolder {

    private static EventSource eventSource;

    public static void setEventSource(EventSource source) {
        eventSource = source;
    }

    public static EventSource getEventSource() {
        return eventSource;
    }
}
