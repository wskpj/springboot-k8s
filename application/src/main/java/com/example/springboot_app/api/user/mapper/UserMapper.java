package com.example.springboot_app.api.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.lib.jpa.core.dto.Paged;
import com.example.lib.jpa.core.dto.SearchParam;
import com.example.lib.jpa.core.enums.SearchType;
import com.example.springboot_app.api.common.dto.SearchRequest;
import com.example.springboot_app.api.user.dto.UserResponse;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.global.mapper.PageMapper;

/**
 * User 도메인 관련 객체 간의 변환을 담당하는 매퍼입니다.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper extends PageMapper {

    /**
     * User 엔티티를 UserInfo 응답 DTO로 변환합니다.
     */
    UserResponse.UserInfo toUserInfo(User user);

    /**
     * User 엔티티 리스트를 UserInfo DTO 리스트로 변환합니다.
     */
    List<UserResponse.UserInfo> toUserInfoList(List<User> users);

    /**
     * API 검색 요청 DTO를 도메인 검색 파라미터로 변환합니다.
     * 필드명이 같은 것들은 자동으로 매핑되며, 타입이 다른 pageable과 type은 
     * 하단의 default 메서드들을 통해 자동으로 변환됩니다.
     */
    @Mapping(target = "pageable", source = "request")
    @Mapping(target = "type", source = "type")
    SearchParam toSearchParam(SearchRequest request);

    /**
     * String 타입의 검색 방식을 SearchType Enum으로 변환합니다.
     */
    default SearchType mapToSearchType(String type) {
        return SearchType.from(type);
    }

    /**
     * SearchRequest 전체 객체를 Spring Data Pageable로 변환합니다.
     */
    default Pageable mapToPageable(SearchRequest request) {
        Sort.Direction dir = "ASC".equalsIgnoreCase(request.direction()) 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        
        return PageRequest.of(
            request.page(), 
            request.size(), 
            Sort.by(dir, request.sortBy())
        );
    }

    /**
     * Page<User> 결과를 Paged<UserInfo> DTO로 변환합니다.
     */
    default Paged<UserResponse.UserInfo> toPagedUserInfo(Page<User> page) {
        return mapPaged(page, toUserInfoList(page.getContent()));
    }
}
