package store.sonyk9919.api.global.jwt.service;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.sonyk9919.api.global.jwt.dto.ClaimsConvertible;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    public void setUp() {
        jwtProvider = new JwtProvider("ZiF5Qkeoy20Fk/iVa3MiQyc50tz5sm7S2i6UER7QkAo=");
    }

    @Test
    @DisplayName("토큰의 발급과 추출이 정상적으로 수행되어야 함")
    public void createJwt() {
        User user = User.from(0L, "son", 28);
        Claims fromUser = jwtProvider.createClaims(user);
        String jwt = jwtProvider.createJwt(fromUser, 10 * 1000);
        Claims fromJwt = jwtProvider.parseJwt(jwt);

        assertThat(fromJwt.getSubject()).isEqualTo(fromUser.getSubject());
        assertThat(fromJwt.get(User.ClaimsKey.NAME.getKey())).isEqualTo(fromUser.get(User.ClaimsKey.NAME.getKey()));
        assertThat(fromJwt.get(User.ClaimsKey.AGE.getKey())).isEqualTo(fromUser.get(User.ClaimsKey.AGE.getKey()));
    }

    @Test
    @DisplayName("만료된 토큰을 추출할 경우 에러가 발생해야 함")
    public void expireJwt() {
        User user = User.from(0L, "son", 28);
        Claims fromUser = jwtProvider.createClaims(user);
        String jwt = jwtProvider.createJwt(fromUser, -1 * 1000);
        assertThatThrownBy(() -> jwtProvider.parseJwt(jwt)).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("변조된 토큰을 추출할 경우 에러가 발생해야 함")
    public void invalidJwt() {
        User user = User.from(0L, "son", 28);
        Claims fromUser = jwtProvider.createClaims(user);
        String jwt = jwtProvider.createJwt(fromUser, 10 * 1000);
        assertThatThrownBy(() -> jwtProvider.parseJwt(jwt + "invalid")).isInstanceOf(CustomException.class);
    }

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static class User implements ClaimsConvertible {

        private final Long id;
        private final String name;
        private final int age;

        public static User from(Long id, String name, int age) {
            return new User(id, name, age);
        }

        @Override
        public String getSubject() {
            return String.valueOf(id);
        }

        @Override
        public Map<String, Object> getBody() {
            return Map.of(
                    ClaimsKey.NAME.getKey(), name,
                    ClaimsKey.AGE.getKey(), age
            );
        }

        @Getter
        @RequiredArgsConstructor
        public enum ClaimsKey {
            NAME("name"),
            AGE("age");

            private final String key;
        }
    }
}