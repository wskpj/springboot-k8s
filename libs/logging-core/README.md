# logging-core

시스템 전반의 로깅 표준과 관측성(Observability)을 위한 상수 및 인터페이스를 정의합니다.

## 주요 구성 요소

1.  **Logging Constants**:
    *   `LoggingConstants`: MDC 키 값(Trace ID, User ID 등)과 공통 로그 패턴에 사용되는 상수를 관리합니다.

## 아키텍처 역할

여러 모듈에서 동일한 MDC 키를 사용하여 로그 추적의 일관성을 보장합니다.
