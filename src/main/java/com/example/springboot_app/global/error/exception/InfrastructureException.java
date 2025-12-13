package com.example.springboot_app.global.error.exception;

import com.example.springboot_app.global.error.ErrorType;

/**
 * 외부 인프라 서비스 관련 예외 (예: Redis 서버 장애, DB 커넥션 오류)
 * 로그 레벨: ERROR (긴급 알림 대상)
 */
public class InfrastructureException extends BaseException {
    public InfrastructureException(ErrorType errorCode) {
        super(errorCode);
    }

    public InfrastructureException(ErrorType errorCode, Object details) {
        super(errorCode, details);
    }
}
