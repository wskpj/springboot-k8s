package com.example.springboot_app.global.exception.types;

import com.example.springboot_app.global.exception.enums.ErrorType;

/**
 * 데이터베이스, 외부 API, 파일 시스템 등 인프라스트럭처 수준의 예외
 * 로그 레벨: ERROR
 */
public class InfrastructureException extends BaseException {
    public InfrastructureException(ErrorType errorType) {
        super(errorType);
    }

    public InfrastructureException(ErrorType errorType, Object details) {
        super(errorType, details);
    }
}
