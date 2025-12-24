# security-starter

Spring Security 설정을 추상화하고, 어노테이션 기반의 직관적인 인가(Authorization) 기능을 제공합니다.

## 주요 기능

1.  **Annotation-based Security**:
    *   `@AuthAdmin`: 관리자 권한(`ROLE_ADMIN`)이 필요한 API에 부여합니다.
    *   `@AuthSelf`: 본인의 리소스에만 접근 가능한 API에 부여합니다. (ID 검증 로직 포함)
    *   `@AuthPublic`: 인증 없이 접근 가능한 API를 명시합니다.

2.  **JWT (JSON Web Token) Management**:
    *   `JwtProvider`를 통한 토큰 생성 및 검증 기능을 자동으로 설정합니다.
    *   `JwtAuthenticationFilter`가 모든 요청의 토큰을 검증하고 `UserContext`를 채웁니다.

3.  **Exception Handling**:
    *   인증 실패(401) 및 인가 실패(403) 시 시스템 공통 응답 규격(`ApiResult`)으로 에러를 반환합니다.

## 사용 방법

```gradle
implementation project(':libs:security-starter')
```

컨트롤러에서 사용 예시:

```java
@AuthAdmin
@GetMapping("/admin/users")
public List<User> getAllUsers() { ... }
```
