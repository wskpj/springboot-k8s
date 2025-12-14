package com.example.springboot_app.domain.auth.dto;

public class AuthParam {
    public record Signup(String email, String password, String name) {}
    public record Login(String email, String password) {}
}
