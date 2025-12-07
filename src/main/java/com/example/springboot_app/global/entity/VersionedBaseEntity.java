package com.example.springboot_app.global.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class VersionedBaseEntity extends BaseEntity {

    @Version
    private Long version;
}
