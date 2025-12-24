# jpa-starter

JPA 사용 시 반복되는 설정과 엔티티 공통 로직을 자동화합니다.

## 주요 기능

1.  **Base Entity**:
    *   `BaseEntity`: 생성일, 수정일, 생성자, 수정자 필드를 포함하며 `AuditingEntityListener`를 통해 자동 관리됩니다.
    *   `VersionedBaseEntity`: 낙관적 락을 위한 `@Version` 필드를 포함합니다.

2.  **Optimistic Lock Retry (AOP)**:
    *   `@OptimisticLock` 어노테이션을 메서드에 부여하면, 낙관적 락 충돌 발생 시 설정된 횟수만큼 자동 재시도합니다.

3.  **Pagination & Search Utilities**:
    *   `Paged<T>`: 표준 페이징 응답 DTO를 제공합니다.
    *   `SearchSpecification`: 동적 쿼리 작성을 위한 Specification 헬퍼를 제공합니다.

## 사용 방법

```gradle
implementation project(':libs:jpa-starter')
```

엔티티 클래스에서 상속하여 사용:

```java
@Entity
public class MyEntity extends BaseEntity { ... }
```
