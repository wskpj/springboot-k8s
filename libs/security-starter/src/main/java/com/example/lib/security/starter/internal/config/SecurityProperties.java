package com.example.lib.security.starter.internal.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 시큐리티 관련 외부 설정 프로퍼티입니다.
 * (CORS 설정은 stone.web.cors로 통합되었습니다.)
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    // 향후 JWT 관련 설정 등을 여기에 추가합니다.
}
