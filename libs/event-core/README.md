# event-core

시스템 내외부의 이벤트 기반 통신을 위한 메시지 규격과 발행 인터페이스를 정의합니다.

## 주요 구성 요소

1.  **Event Models**:
    *   `BaseEvent`: 모든 이벤트의 공통 속성(ID, 발생 시간, 소스 등)을 정의합니다.
    *   `BaseDomainEvent`: 비즈니스 도메인 상태 변경 이벤트를 위한 기반 클래스입니다.
    *   `BaseSystemEvent`: 시스템 로그, 알림 등 기술적 이벤트를 위한 기반 클래스입니다.

2.  **Publisher Interface**:
    *   `EventPublisher`: 특정 구현 기술에 관계없이 이벤트를 발행할 수 있는 통합 인터페이스를 제공합니다.

3.  **Event Metadata**:
    *   `EventType`, `EventSource`: 이벤트의 종류와 발생지를 식별하기 위한 인터페이스입니다.

## 아키텍처 역할

서비스 간 결합도를 낮추기 위한 이벤트 주도 설계(Event-Driven Design)의 기반이 됩니다.
