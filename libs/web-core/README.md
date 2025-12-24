# web-core

웹 애플리케이션의 공통 응답 규격, 에러 모델, 그리고 웹 계층에서 필요한 핵심 추상화 계층을 정의합니다.

## 주요 구성 요소

1.  **Response Model**:
    *   `ApiResult`: 모든 API 응답의 표준 래퍼 클래스입니다. (성공 여부, 데이터, 트레이스 ID 포함)
    *   `ApiError`: 에러 발생 시 상세 정보를 담는 모델입니다.

2.  **Dispatcher & Strategy**:
    *   `ApiResultDispatcher`: 응답 객체나 예외를 `ApiResult`로 변환하는 핵심 인터페이스입니다.
    *   `ExceptionHandleStrategy`: 다양한 예외를 표준 에러 모델로 변환하기 위한 전략 인터페이스입니다.

3.  **Core Filters & Utilities**:
    *   `ResponseFilter`: 자동 응답 래핑 대상 여부를 결정하는 필터 인터페이스입니다.
    *   `IpUtil`, `CookieManager`: 웹 인프라 관련 유틸리티 인터페이스입니다.

## 아키텍처 역할

본 모듈은 기술적 구현(Spring MVC 등)보다는 **웹 응답의 규격과 처리 정책**을 정의하는 데 집중합니다. 실제 자동 설정 및 구현체는 `web-starter`에서 제공합니다.
