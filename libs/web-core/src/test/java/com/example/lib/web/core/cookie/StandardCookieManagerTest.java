package com.example.lib.web.core.cookie;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StandardCookieManager 테스트")
class StandardCookieManagerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private StandardCookieManager cookieManager;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        cookieManager = new StandardCookieManager(request, response);
    }

    @Nested
    @DisplayName("addCookie()")
    class AddCookie {

        @Test
        @DisplayName("쿠키가 응답에 추가된다")
        void addsCookieToResponse() {
            cookieManager.addCookie("token", "abc123", 3600);

            Cookie cookie = response.getCookie("token");
            assertThat(cookie).isNotNull();
            assertThat(cookie.getValue()).isEqualTo("abc123");
        }

        @Test
        @DisplayName("쿠키는 HttpOnly, path=/, 지정한 maxAge로 설정된다")
        void setsCorrectCookieAttributes() {
            cookieManager.addCookie("session", "xyz", 1800);

            Cookie cookie = response.getCookie("session");
            assertThat(cookie).isNotNull();
            assertThat(cookie.isHttpOnly()).isTrue();
            assertThat(cookie.getPath()).isEqualTo("/");
            assertThat(cookie.getMaxAge()).isEqualTo(1800);
        }

        @Test
        @DisplayName("서로 다른 이름의 쿠키를 여러 개 추가할 수 있다")
        void addsMultipleCookies() {
            cookieManager.addCookie("a", "1", 100);
            cookieManager.addCookie("b", "2", 200);

            assertThat(response.getCookie("a")).isNotNull();
            assertThat(response.getCookie("b")).isNotNull();
        }
    }

    @Nested
    @DisplayName("removeCookie()")
    class RemoveCookie {

        @Test
        @DisplayName("삭제 쿠키는 maxAge=0으로 응답에 추가된다")
        void setsCookieMaxAgeToZero() {
            cookieManager.removeCookie("token");

            Cookie cookie = response.getCookie("token");
            assertThat(cookie).isNotNull();
            assertThat(cookie.getMaxAge()).isEqualTo(0);
        }

        @Test
        @DisplayName("삭제 쿠키는 path=/, HttpOnly 속성을 유지한다")
        void keepsCookieAttributes() {
            cookieManager.removeCookie("session");

            Cookie cookie = response.getCookie("session");
            assertThat(cookie.getPath()).isEqualTo("/");
            assertThat(cookie.isHttpOnly()).isTrue();
        }
    }

    @Nested
    @DisplayName("getCookie()")
    class GetCookie {

        @Test
        @DisplayName("요청에 쿠키가 없으면 Optional.empty()를 반환한다")
        void returnsEmptyWhenNoCookies() {
            Optional<String> result = cookieManager.getCookie("token");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("요청에 해당 이름의 쿠키가 있으면 값을 반환한다")
        void returnsCookieValueByName() {
            request.setCookies(new Cookie("token", "my-token-value"));

            Optional<String> result = cookieManager.getCookie("token");

            assertThat(result).isPresent().contains("my-token-value");
        }

        @Test
        @DisplayName("이름이 다른 쿠키만 있으면 Optional.empty()를 반환한다")
        void returnsEmptyWhenNameDoesNotMatch() {
            request.setCookies(new Cookie("other", "value"));

            Optional<String> result = cookieManager.getCookie("token");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("여러 쿠키 중 정확히 이름이 일치하는 쿠키의 값을 반환한다")
        void returnsMatchingCookieAmongMultiple() {
            request.setCookies(
                new Cookie("a", "value-a"),
                new Cookie("target", "value-target"),
                new Cookie("b", "value-b")
            );

            Optional<String> result = cookieManager.getCookie("target");

            assertThat(result).isPresent().contains("value-target");
        }
    }
}
