package com.example.springboot_app.infrastructure.redis.dto;

import java.util.Arrays;

public record ValueBinding(String value) {
    public static ValueBinding of(Object... args) {
        String combined = String.join(":",
                Arrays.stream(args)
                        .map(String::valueOf)
                        .toArray(String[]::new));
        return new ValueBinding(combined);
    }

    public String value() {
        return value;
    }
}
