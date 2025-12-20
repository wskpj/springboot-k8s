package com.example.springboot_app.domain.common.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;

@Getter
@MappedSuperclass
@Deprecated
public abstract class VersionedBaseEntity extends BaseEntity {

    @Version
    private Long version;
}
