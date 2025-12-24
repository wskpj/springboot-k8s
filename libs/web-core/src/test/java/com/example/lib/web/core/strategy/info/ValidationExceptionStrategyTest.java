package com.example.lib.web.core.strategy.info;

import com.example.lib.web.core.response.ApiError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidationExceptionStrategy 테스트")
class ValidationExceptionStrategyTest {

    private final ValidationExceptionStrategy strategy = new ValidationExceptionStrategy();

    /**
     * MethodArgumentNotValidException 생성 헬퍼.
     * BeanPropertyBindingResult에 직접 필드 에러를 추가하여 생성합니다.
     */
    private MethodArgumentNotValidException buildException(String field, Object rejected, String message) {
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(new Object(), "target");
        result.addError(new FieldError("target", field, rejected, false, null, null, message));
        return new MethodArgumentNotValidException(null, result);
    }

    @Nested
    @DisplayName("supports()")
    class Supports {

        @Test
        @DisplayName("MethodArgumentNotValidException에 대해 true를 반환한다")
        void supportsValidationException() {
            MethodArgumentNotValidException ex = buildException("email", "bad", "이메일 형식 오류");
            assertThat(strategy.supports(ex)).isTrue();
        }

        @Test
        @DisplayName("일반 RuntimeException에 대해 false를 반환한다")
        void doesNotSupportRuntimeException() {
            assertThat(strategy.supports(new RuntimeException())).isFalse();
        }
    }

    @Nested
    @DisplayName("handle()")
    class Handle {

        @Test
        @DisplayName("handle()은 400 BAD_REQUEST로 응답한다")
        void returns400BadRequest() {
            MethodArgumentNotValidException ex = buildException("name", "", "이름 필수");
            ApiError error = strategy.handle(ex, "/api/signup");

            assertThat(error.status()).isEqualTo(400);
            assertThat(error.code()).isEqualTo("G400");
            assertThat(error.instance()).isEqualTo("/api/signup");
        }

        @Test
        @DisplayName("FieldError 목록이 details에 List<ApiError.FieldError>로 담긴다")
        void fieldErrorsAreIncludedInDetails() {
            MethodArgumentNotValidException ex = buildException("email", "not-email", "이메일 형식 오류");
            ApiError error = strategy.handle(ex, "/api/test");

            assertThat(error.details()).isInstanceOf(List.class);
            @SuppressWarnings("unchecked")
            List<ApiError.FieldError> fieldErrors = (List<ApiError.FieldError>) error.details();
            assertThat(fieldErrors).hasSize(1);
            assertThat(fieldErrors.get(0).field()).isEqualTo("email");
            assertThat(fieldErrors.get(0).value()).isEqualTo("not-email");
            assertThat(fieldErrors.get(0).reason()).isEqualTo("이메일 형식 오류");
        }

        @Test
        @DisplayName("rejectedValue가 null이면 빈 문자열로 처리된다")
        void nullRejectedValueBecomesEmptyString() {
            MethodArgumentNotValidException ex = buildException("name", null, "이름 필수");
            ApiError error = strategy.handle(ex, "/api/test");

            @SuppressWarnings("unchecked")
            List<ApiError.FieldError> fieldErrors = (List<ApiError.FieldError>) error.details();
            assertThat(fieldErrors.get(0).value()).isEqualTo("");
        }

        @Test
        @DisplayName("여러 필드 에러가 모두 포함된다")
        void multipleFieldErrorsAreAllIncluded() {
            BeanPropertyBindingResult result = new BeanPropertyBindingResult(new Object(), "target");
            result.addError(new FieldError("target", "email", null, false, null, null, "이메일 필수"));
            result.addError(new FieldError("target", "password", "123", false, null, null, "비밀번호 최소 8자"));
            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, result);

            ApiError error = strategy.handle(ex, "/api/test");

            @SuppressWarnings("unchecked")
            List<ApiError.FieldError> fieldErrors = (List<ApiError.FieldError>) error.details();
            assertThat(fieldErrors).hasSize(2);
        }
    }
}
