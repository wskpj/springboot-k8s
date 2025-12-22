package com.example.lib.jpa.starter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 기능을 활성화하는 설정 클래스입니다.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "jpaAuditorAware")
public class JpaAuditingConfig {
}
