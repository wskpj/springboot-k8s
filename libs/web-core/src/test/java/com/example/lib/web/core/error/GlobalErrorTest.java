package com.example.lib.web.core.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GlobalError 테스트")
class GlobalErrorTest {

    @Test
    @DisplayName("각 에러의 HTTP 상태 코드가 정확히 매핑된다")
    void httpStatusMappings() {
        assertThat(GlobalError.BAD_REQUEST.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(GlobalError.UNAUTHORIZED.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(GlobalError.FORBIDDEN.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(GlobalError.NOT_FOUND.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(GlobalError.CONFLICT.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(GlobalError.INTERNAL_SERVER_ERROR.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("각 에러의 코드가 'G' 접두사와 상태 코드로 구성된다")
    void codePrefixConvention() {
        for (GlobalError error : GlobalError.values()) {
            assertThat(error.getCode())
                .as("코드는 'G'로 시작해야 합니다: " + error.name())
                .startsWith("G");
        }
    }

    @Test
    @DisplayName("각 에러의 message가 비어있지 않다")
    void messageIsNotBlank() {
        for (GlobalError error : GlobalError.values()) {
            assertThat(error.getMessage())
                .as("message가 비어있으면 안 됩니다: " + error.name())
                .isNotBlank();
        }
    }

    @Test
    @DisplayName("INTERNAL_SERVER_ERROR는 fallback 전략에서 사용되는 기본 에러이다")
    void internalServerErrorIsDefaultFallback() {
        assertThat(GlobalError.INTERNAL_SERVER_ERROR.getStatus()).isEqualTo(500);
        assertThat(GlobalError.INTERNAL_SERVER_ERROR.getCode()).isEqualTo("G500");
    }
}
