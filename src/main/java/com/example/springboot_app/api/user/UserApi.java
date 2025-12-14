package com.example.springboot_app.api.user;

import com.example.springboot_app.api.common.dto.SearchRequest;
import com.example.springboot_app.api.user.dto.UserResponse;
import com.example.springboot_app.domain.common.dto.Paged;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "User Management", description = "APIs for user information and search")
@RequestMapping("/api/v1/users")
public interface UserApi {

    @Operation(summary = "Search Users", description = "Searches users with pagination, keyword search, and filters")
    @GetMapping
    Paged<UserResponse.UserInfo> searchUsers(SearchRequest request);
}
