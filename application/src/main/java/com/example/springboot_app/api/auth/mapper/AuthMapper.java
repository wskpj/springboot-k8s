package com.example.springboot_app.api.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.springboot_app.api.auth.dto.AuthRequest;
import com.example.springboot_app.api.auth.dto.AuthResponse;
import com.example.springboot_app.domain.auth.dto.AuthParam;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.global.mapper.PagedMapper;

/**
 * Auth 도메인 관련 객체 간의 변환을 담당하는 매퍼입니다.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuthMapper extends PagedMapper {

    /**
     * Signup 요청 DTO를 도메인 파라미터로 변환합니다.
     */
    AuthParam.Signup toSignupParam(AuthRequest.Signup request);

    /**
     * Login 요청 DTO를 도메인 파라미터로 변환합니다.
     */
    AuthParam.Login toLoginParam(AuthRequest.Login request);

    /**
     * 도메인 토큰 결과를 응답 DTO로 변환합니다.
     */
    AuthResponse.Token toTokenResponse(AuthResult.Token result);

    /**
     * 도메인 사용자 정보를 응답 DTO로 변환합니다.
     */
    AuthResponse.UserInfo toUserInfoResponse(AuthResult.UserInfo result);
}
