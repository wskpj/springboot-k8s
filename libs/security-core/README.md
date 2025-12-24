# security-core

보안 및 인가 처리를 위한 핵심 어노테이션과 인증 정보 인터페이스를 정의합니다.

## 주요 구성 요소

1.  **Security Annotations**:
    *   `@AuthAdmin`: 관리자 전용 기능을 명시합니다.
    *   `@AuthSelf`: 사용자 본인의 데이터 접근 권한을 명시합니다.
    *   `@AuthPublic`: 인증이 불필요한 공개 API임을 명시합니다.

2.  **Authentication Interfaces**:
    *   `TokenProvider`: 인증 토큰의 생성 및 검증을 위한 추상 인터페이스입니다.
    *   `AuthResolver`: 요청에서 인증 정보를 추출하기 위한 해결사 인터페이스입니다.

## 아키텍처 역할

비즈니스 로직(Controller, Service)에서 구체적인 보안 기술(Spring Security, JWT 등)에 의존하지 않고 권한을 선언적으로 관리할 수 있게 합니다.
