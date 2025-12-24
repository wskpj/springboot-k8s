package com.example.lib.common.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BaseException 계층 테스트")
class BaseExceptionTest {

    private static final ErrorType DOMAIN_TYPE = new ErrorType() {
        @Override public int getStatus() { return 400; }
        @Override public String getCode() { return "D001"; }
        @Override public String getMessage() { return "도메인 에러 메시지"; }
    };

    private static final ErrorType SYSTEM_TYPE = new ErrorType() {
        @Override public int getStatus() { return 500; }
        @Override public String getCode() { return "S001"; }
        @Override public String getMessage() { return "시스템 에러 메시지"; }
    };

    // 테스트용 구체 클래스
    static class ConcreteDomainException extends BaseDomainException {
        ConcreteDomainException() { super(DOMAIN_TYPE); }
        ConcreteDomainException(Object details) { super(DOMAIN_TYPE, details); }
    }

    static class ConcreteSystemException extends BaseSystemException {
        ConcreteSystemException() { super(SYSTEM_TYPE); }
        ConcreteSystemException(Object details) { super(SYSTEM_TYPE, details); }
    }

    @Nested
    @DisplayName("BaseDomainException")
    class DomainException {

        @Test
        @DisplayName("getMessage()는 ErrorType의 message를 반환한다")
        void messageFromErrorType() {
            ConcreteDomainException ex = new ConcreteDomainException();

            assertThat(ex.getMessage()).isEqualTo("도메인 에러 메시지");
            assertThat(ex.getErrorType().getCode()).isEqualTo("D001");
            assertThat(ex.getErrorType().getStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("details를 담아서 생성하면 getDetails()로 꺼낼 수 있다")
        void detailsAreAccessible() {
            ConcreteDomainException ex = new ConcreteDomainException("추가 정보");

            assertThat(ex.getDetails()).isEqualTo("추가 정보");
        }

        @Test
        @DisplayName("fillInStackTrace()가 억제되어 스택 트레이스 생성 비용이 발생하지 않는다")
        void stackTraceIsSuppressed() {
            ConcreteDomainException ex = new ConcreteDomainException();

            // fillInStackTrace 억제 시 getStackTrace()는 빈 배열이거나
            // 스택 트레이스가 채워지지 않아야 한다
            assertThat(ex.getStackTrace()).isEmpty();
        }
    }

    @Nested
    @DisplayName("BaseSystemException")
    class SystemException {

        @Test
        @DisplayName("getMessage()는 ErrorType의 message를 반환한다")
        void messageFromErrorType() {
            ConcreteSystemException ex = new ConcreteSystemException();

            assertThat(ex.getMessage()).isEqualTo("시스템 에러 메시지");
            assertThat(ex.getErrorType().getCode()).isEqualTo("S001");
            assertThat(ex.getErrorType().getStatus()).isEqualTo(500);
        }

        @Test
        @DisplayName("details를 담아서 생성하면 getDetails()로 꺼낼 수 있다")
        void detailsAreAccessible() {
            ConcreteSystemException ex = new ConcreteSystemException("시스템 에러 상세");

            assertThat(ex.getDetails()).isEqualTo("시스템 에러 상세");
        }

        @Test
        @DisplayName("BaseSystemException은 스택 트레이스가 유지된다 (억제되지 않음)")
        void stackTraceIsNotSuppressed() {
            ConcreteSystemException ex = new ConcreteSystemException();

            // 시스템 예외는 스택 트레이스가 있어야 디버깅이 가능하다
            assertThat(ex.getStackTrace()).isNotEmpty();
        }
    }
}
