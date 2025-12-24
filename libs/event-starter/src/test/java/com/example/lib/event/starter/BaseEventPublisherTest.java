package com.example.lib.event.starter;

import com.example.lib.event.core.BaseSystemEvent;
import com.example.lib.event.core.DefaultEventType;
import com.example.lib.event.core.EventSource;
import com.example.lib.event.core.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("BaseEventPublisher 테스트")
class BaseEventPublisherTest {

    // 테스트용 구체 이벤트
    static class TestEvent extends BaseSystemEvent {
        TestEvent(EventType type, EventSource source) {
            super(type, source);
        }
    }

    // 테스트용 구체 Publisher (abstract 구현)
    static class ConcretePublisher extends BaseEventPublisher {
        ConcretePublisher(ApplicationEventPublisher publisher) {
            super(publisher);
        }
    }

    private ApplicationEventPublisher mockPublisher;
    private ConcretePublisher publisher;
    private final EventSource testSource = () -> "TEST";
    private final EventType testType = DefaultEventType.of("TEST_001", "테스트");

    @BeforeEach
    void setUp() {
        mockPublisher = mock(ApplicationEventPublisher.class);
        publisher = new ConcretePublisher(mockPublisher);
    }

    @Test
    @DisplayName("publish()는 ApplicationEventPublisher.publishEvent()에 이벤트를 위임한다")
    void delegatesToApplicationEventPublisher() {
        TestEvent event = new TestEvent(testType, testSource);

        publisher.publish(event);

        verify(mockPublisher, times(1)).publishEvent(event);
    }

    @Test
    @DisplayName("publish()는 항상 동일한 이벤트 객체를 그대로 전달한다")
    void passesExactEventInstance() {
        TestEvent event = new TestEvent(testType, testSource);

        publisher.publish(event);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(mockPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue()).isSameAs(event);
    }

    @Test
    @DisplayName("publish()를 여러 번 호출하면 그만큼 publishEvent()가 호출된다")
    void callsPublishEventMultipleTimes() {
        publisher.publish(new TestEvent(testType, testSource));
        publisher.publish(new TestEvent(testType, testSource));
        publisher.publish(new TestEvent(testType, testSource));

        verify(mockPublisher, times(3)).publishEvent(any());
    }
}
