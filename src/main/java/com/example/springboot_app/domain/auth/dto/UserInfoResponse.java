package com.example.springboot_app.domain.auth.dto;

import com.example.springboot_app.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String email;
    private String name;
    private Long accessTokenExpiresIn;
    private Long refreshTokenExpiresIn;

    public UserInfoResponse(User user, Long atExpiresIn, Long rtExpiresIn) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.accessTokenExpiresIn = atExpiresIn;
        this.refreshTokenExpiresIn = rtExpiresIn;
    }
}
