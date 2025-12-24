# logging-starter

애플리케이션의 로깅 관측성을 높이기 위한 MDC 설정 및 컨텍스트 전파 기능을 제공합니다.

## 주요 기능

1.  **MDC Logging**:
    *   `MdcLoggingFilter`가 모든 요청의 트레이스 ID를 로깅 컨텍스트(MDC)에 자동으로 삽입합니다.
    *   로그 패턴에 `%X{traceId}`를 추가하여 요청 추적이 가능합니다.

2.  **Context Propagation**:
    *   `MdcTaskDecorator`를 통해 `@Async` 등 비동기 스레드 실행 시에도 로깅 컨텍스트와 사용자 정보가 유실되지 않고 전파됩니다.

3.  **User Context in Logs**:
    *   `UserContextMdcFilter`를 통해 현재 요청 사용자의 ID를 로그에 포함할 수 있습니다.

## 사용 방법

```gradle
implementation project(':libs:logging-starter')
```
