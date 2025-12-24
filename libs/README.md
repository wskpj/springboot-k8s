# Custom Library Modules

이 디렉토리는 프로젝트 전반에서 사용되는 커스텀 라이브러리 모듈들을 포함합니다. 모든 모듈은 **Core & Starter** 패턴을 따르며, 일관성 있고 자동화된 설계를 지향합니다.

## Core & Starter 패턴

1.  **`-core` 모듈**: 
    *   특정 기술 스택에 종속되지 않는 순수 인터페이스, DTO, 예외 클래스, 엔티티 기명 규칙 등을 정의합니다.
    *   비즈니스 로직의 추상화 계층 역할을 합니다.

2.  **`-starter` 모듈**:
    *   Spring Boot의 `@AutoConfiguration`을 활용하여 `-core`의 기능을 자동으로 빈(Bean)으로 등록하고 설정합니다.
    *   `application` 모듈은 `-starter` 의존성만 추가하면 별도의 설정 없이 모든 기능을 즉시 사용할 수 있습니다.

## 모듈 목록

| 모듈 | 설명 | 주요 기능 |
| :--- | :--- | :--- |
| [common-core](./common-core) | 공통 기반 모듈 | Context(User, Trace) 관리, 공통 예외/에러 인터페이스 |
| [web-starter](./web-starter) | 웹 응답/예외 자동화 | API 응답 규격화(ApiResult), 전역 예외 처리 전략 |
| [jpa-starter](./jpa-starter) | JPA 편의 기능 | 공통 감사 필드(BaseEntity), 낙관적 락 자동 재시도 |
| [security-starter](./security-starter) | 보안 및 인가 | 어노테이션 기반 권한 제어(@AuthAdmin), JWT 관리 |
| [logging-starter](./logging-starter) | 로깅 및 관측성 | MDC 기반 로깅, 트레이스 ID 전파 |
| [event-starter](./event-starter) | 이벤트 인프라 | 표준 이벤트 발행 및 처리 인터페이스 |

---
## Bean 오버라이딩 (Customization)

모든 스타터 모듈은 `@ConditionalOnMissingBean`을 사용하여 설계되었습니다. 만약 라이브러리에서 제공하는 기본 동작을 변경하고 싶다면, `application` 모듈에서 동일한 타입의 빈을 직접 등록하십시오. 

Spring Boot는 사용자 정의 빈을 우선적으로 등록하며, 이 경우 라이브러리의 자동 설정 빈은 생성되지 않습니다. 이를 통해 비즈니스 요구사항에 맞게 라이브러리 기능을 손쉽게 확장하거나 교체할 수 있습니다.

---
**Tip:** 각 모듈의 상세 내용은 해당 디렉토리의 `README.md`를 참고하세요.
