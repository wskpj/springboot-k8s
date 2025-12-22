package com.example.lib.jpa.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;

/**
 * 낙관적 락(Optimistic Lock)을 지원하는 기반 클래스입니다.
 * 모든 비즈니스 엔티티는 데이터 정합성을 위해 이 클래스를 상속받는 것을 원칙으로 합니다.
 */
@Getter
@MappedSuperclass
public abstract class VersionedBaseEntity extends BaseEntity {

    @Version
    @Column(nullable = false)
    private Long version;
}
