package com.example.springboot_app.infrastructure.redis.dto;

import java.util.Arrays;

public record ValueBinding(String value) {
    
    public static ValueBinding plain(Object value) {
        return new ValueBinding(String.valueOf(value));
    }

    public static ValueBinding composite(Object... args) {
        String combined = String.join(":",
                Arrays.stream(args)
                        .map(String::valueOf)
                        .toArray(String[]::new));
        return new ValueBinding(combined);
    }

    public static ValueBinding of(Object... args) {
        if (args.length == 1) {
            return plain(args[0]);
        }
        return composite(args);
    }

    public String value() {
        return value;
    }
}
