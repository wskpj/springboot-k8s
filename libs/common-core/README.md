# common-core

시스템 전반에서 사용되는 공통 도메인 모델, 문맥(Context) 관리, 그리고 표준 예외 규격을 제공합니다.

## 주요 기능

1.  **Context Management**:
    *   `UserContext`: 현재 요청을 수행하는 사용자 정보를 스레드 로컬로 관리합니다.
    *   `TraceContext`: 요청별 고유 트레이스 ID를 관리하여 로깅 및 추적에 활용합니다.
    *   `ContextHolder`: `UserContextHolder`, `TraceContextHolder`를 통해 전역에서 안전하게 접근 가능합니다. (익명 사용자 기본 지원)

2.  **Standard Exceptions**:
    *   `BaseException`: 시스템의 모든 커스텀 예외의 최상위 클래스입니다.
    *   `ErrorType`: 에러 코드, 메시지, HTTP 상태 코드를 표준화하기 위한 인터페이스입니다.

3.  **Utility Models**:
    *   공통적으로 사용되는 데이터 모델 및 상수 정의.

## 사용 방법

다른 모듈이나 application에서 의존성을 추가하여 사용합니다.

```gradle
implementation project(':libs:common-core')
```
