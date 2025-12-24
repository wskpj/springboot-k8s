# web-starter

Spring MVC 기반 웹 애플리케이션의 응답 규격화 및 예외 처리를 자동화합니다.

## 주요 기능

1.  **Response Wrapping (Auto)**:
    *   `StandardResponseHandler`가 모든 컨트롤러의 반환값을 `ApiResult<T>` 규격으로 자동 래핑합니다.
    *   컨트롤러에서 직접 래핑할 필요가 없어 비즈니스 로직에만 집중할 수 있습니다.

2.  **Global Exception Handling**:
    *   `ExceptionHandleStrategy` 패턴을 통해 예외별 처리 로직이 분리되어 있습니다.
    *   모든 예외는 규격화된 에러 응답(`ApiResult.fail()`)으로 변환됩니다.

3.  **Web Utilities**:
    *   `IpUtil`, `CookieManager` 등 웹 환경에서 필요한 편의 기능을 제공합니다.

## 설정 (application.yml)

```yaml
lib:
  web:
    response-filter-prefixes: # 자동 응답 래핑을 적용할 패키지 경로
      - com.example.springboot_app.api
```

## 사용 방법

스타터 의존성을 추가하면 자동 설정이 활성화됩니다.

```gradle
implementation project(':libs:web-starter')
```
