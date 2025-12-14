package com.example.springboot_app.domain.user.service;

import com.example.springboot_app.domain.common.dto.SearchParam;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.global.util.JpaUtil;
import com.example.springboot_app.global.util.SearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * SearchParam을 기반으로 동적 검색을 수행하고 분리된 Page 객체를 반환합니다.
     */
    @Transactional(readOnly = true)
    public Page<User> searchUsers(SearchParam param) {
        Specification<User> spec = SearchSpecification.build(param);
        return userRepository.findAll(spec, JpaUtil.validatePageable(param.pageable(), User.class));
    }
}
