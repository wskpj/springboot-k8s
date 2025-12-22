package com.example.springboot_app.api.user;

import org.springframework.web.bind.annotation.RestController;

import com.example.lib.jpa.core.dto.Paged;
import com.example.springboot_app.api.common.dto.SearchRequest;
import com.example.springboot_app.api.user.dto.UserResponse;
import com.example.springboot_app.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public Paged<UserResponse.UserInfo> searchUsers(SearchRequest request) {
        return Paged.from(
            userService.searchUsers(request.toParam()),
            UserResponse.UserInfo::from
        );
    }
}
