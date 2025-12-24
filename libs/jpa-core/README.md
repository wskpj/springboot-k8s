# jpa-core

데이터 영속성 계층에서 공통으로 사용되는 기반 엔티티, DTO, 그리고 쿼리 추상화 도구를 정의합니다.

## 주요 구성 요소

1.  **Base Entities**:
    *   `BaseEntity`: `createdAt`, `updatedAt`, `createdBy`, `updatedBy` 필드를 포함한 추상 클래스입니다.
    *   `VersionedBaseEntity`: 낙관적 락 처리를 위한 `@Version` 필드가 추가된 기반 클래스입니다.

2.  **Pagination DTOs**:
    *   `Paged<T>`: 페이징 데이터와 메타데이터(총 개수, 페이지 정보 등)를 담는 표준 모델입니다.
    *   `SearchParam`: 검색 조건과 페이징 요청을 결합한 공통 파라미터 모델입니다.

3.  **Annotations**:
    *   `@OptimisticLock`: 낙관적 락 재시도 기능을 활성화하기 위한 마커 어노테이션입니다.

## 아키텍처 역할

JPA 엔티티 설계 시 일관된 규칙을 적용하고, 계층 간 데이터 전달 시 페이징 규격을 통일하기 위한 용도로 사용됩니다.
