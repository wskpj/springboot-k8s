package com.example.springboot_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringbootAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAppApplication.class, args);
	}

}
