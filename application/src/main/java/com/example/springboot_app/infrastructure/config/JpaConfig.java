package com.example.springboot_app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.lib.event.core.EventSource;
import com.example.springboot_app.global.event.ApplicationEventSource;

@Configuration
public class JpaConfig {

    @Bean
    public EventSource jpaEventSource() {
        return ApplicationEventSource.JPA;
    }
}
