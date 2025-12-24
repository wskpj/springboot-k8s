package com.example.springboot_app.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.lib.jpa.core.dto.Paged;
import com.example.lib.security.starter.annotation.AuthAdmin;
import com.example.springboot_app.api.common.dto.SearchRequest;
import com.example.springboot_app.api.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "User Management", description = "APIs for user information and search")
@RequestMapping("/api/v1/users")
public interface UserApi {

    @AuthAdmin
    @Operation(summary = "Search Users", description = "Searches users with pagination, keyword search, and filters")
    @GetMapping
    Paged<UserResponse.UserInfo> searchUsers(SearchRequest request);
}
