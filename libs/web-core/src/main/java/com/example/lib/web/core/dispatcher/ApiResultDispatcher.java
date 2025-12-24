package com.example.lib.web.core.dispatcher;

import com.example.lib.web.core.response.ApiResult;

/**
 * 어떤 입력값이든 표준 응답 규격(ApiResult)으로 변환하여 내보내는 통합 창구입니다.
 */
public interface ApiResultDispatcher {

    /**
     * 정상 데이터를 표준 응답 규격으로 변환하여 송출합니다.
     */
    <T> ApiResult<T> dispatch(T data);

    /**
     * 발생한 예외를 분석하여 표준 에러 응답 규격으로 변환하여 송출합니다.
     */
    ApiResult<?> dispatch(Exception e, String uri);
}
