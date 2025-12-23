package com.example.springboot_app.api.user;

import org.springframework.web.bind.annotation.RestController;

import com.example.lib.jpa.core.dto.Paged;
import com.example.springboot_app.api.common.dto.SearchRequest;
import com.example.springboot_app.api.user.dto.UserResponse;
import com.example.springboot_app.api.user.mapper.UserMapper;
import com.example.springboot_app.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public Paged<UserResponse.UserInfo> searchUsers(SearchRequest request) {
        return userMapper.toPagedUserInfo(
            userService.searchUsers(userMapper.toSearchParam(request))
        );
    }
}
