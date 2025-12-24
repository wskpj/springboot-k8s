package com.example.springboot_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.springboot_app.infrastructure.config.AppProperties;

@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication(scanBasePackages = {"com.example"})
public class SpringbootAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAppApplication.class, args);
	}

}
