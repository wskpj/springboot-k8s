package com.example.springboot_app.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import com.example.lib.jpa.starter.config.JpaStarterConfig;
import com.example.springboot_app.global.event.ApplicationEventSource;

@Configuration
public class JpaConfig extends JpaStarterConfig {

    public JpaConfig() {
        super(ApplicationEventSource.JPA);
    }
}
