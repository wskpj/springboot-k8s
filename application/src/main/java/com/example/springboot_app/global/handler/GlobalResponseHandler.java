package com.example.springboot_app.global.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.lib.web.starter.bean.ApiGenerator;
import com.example.lib.web.starter.handler.BaseResponseHandler;

/**
 * 모든 REST 컨트롤러의 응답을 가로채어 ApiResult 규격으로 래핑합니다.
 */
@RestControllerAdvice(basePackages = "com.example.springboot_app")
public class GlobalResponseHandler extends BaseResponseHandler {

    public GlobalResponseHandler(ApiGenerator apiGenerator) {
        super(apiGenerator);
    }
}
