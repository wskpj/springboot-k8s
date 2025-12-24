# event-starter

시스템 내부 이벤트를 표준화된 방식으로 발행하고 처리하기 위한 인프라를 제공합니다.

## 주요 기능

1.  **Standard Event Model**:
    *   `BaseEvent`: 모든 이벤트의 기반 클래스입니다.
    *   `BaseDomainEvent`, `BaseSystemEvent`로 목적에 맞게 확장 가능합니다.

2.  **Event Publisher**:
    *   `EventPublisher` 인터페이스를 통해 일관된 방식으로 이벤트를 발행합니다.
    *   기본 구현으로 Spring `ApplicationEventPublisher`를 사용하며, 추후 외부 메시지 브로커로 확장하기 용이한 구조입니다.

3.  **Context Aware Events**:
    *   이벤트 발생 시점의 트레이스 정보나 발행자 정보를 포함할 수 있는 규격을 제공합니다.

## 사용 방법

```gradle
implementation project(':libs:event-starter')
```

이벤트 발행 예시:

```java
@Service
@RequiredArgsConstructor
public class MyService {
    private final EventPublisher eventPublisher;

    public void doSomething() {
        eventPublisher.publish(new MyCustomEvent(...));
    }
}
```
