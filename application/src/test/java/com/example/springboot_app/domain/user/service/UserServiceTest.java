package com.example.springboot_app.domain.user.service;

import com.example.lib.jpa.core.dto.SearchParam;
import com.example.lib.jpa.core.enums.SearchType;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.user.entity.UserRole;
import com.example.springboot_app.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                .email("test1@example.com")
                .name("Alpha")
                .password("password")
                .role(UserRole.USER)
                .build());
        userRepository.save(User.builder()
                .email("test2@example.com")
                .name("Beta")
                .password("password")
                .role(UserRole.USER)
                .build());
    }

    @Test
    @DisplayName("이름으로 유저를 검색한다 (CONTAINS)")
    void searchUsersByNameTest() {
        // given
        SearchParam param = new SearchParam(
                PageRequest.of(0, 10),
                "Alp",
                List.of("name"),
                SearchType.CONTAINS,
                null,
                null
        );

        // when
        Page<User> result = userService.searchUsers(param);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Alpha");
    }

    @Test
    @DisplayName("이메일로 유저를 검색한다 (EQUALS)")
    void searchUsersByEmailTest() {
        // given
        SearchParam param = new SearchParam(
                PageRequest.of(0, 10),
                "test2@example.com",
                List.of("email"),
                SearchType.EQUALS,
                null,
                null
        );

        // when
        Page<User> result = userService.searchUsers(param);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("test2@example.com");
    }
}
