package store.sonyk9919.api.domain.auth;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import store.sonyk9919.api.domain.auth.dto.AuthDto;
import store.sonyk9919.api.domain.auth.filter.JwtAuthenticationFilter;
import store.sonyk9919.api.domain.member.entitiy.AccountRole;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthE2ETest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입부터 로그인, 권한이 부여된 API 요청까지의 전체 흐름을 검증한다")
    void authFlowTest() throws Exception {
        String id = "testUser";
        String password = "password123!";
        AuthDto authDto = AuthDto.from(id, password);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDto)))
                .andExpect(status().isCreated());

        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDto)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(result -> {
                    String setCookie = result.getResponse().getHeader(HttpHeaders.SET_COOKIE);
                    assertThat(setCookie).contains("accessToken");
                    assertThat(setCookie).contains("HttpOnly");
                    assertThat(setCookie).contains("Secure");
                })
                .andReturn();

        Cookie[] cookies = mvcResult.getResponse().getCookies();
        mockMvc.perform(get("/auth/test/user").cookie(cookies))
                .andExpect(status().isOk());

        mockMvc.perform(get("/auth/test/admin").cookie(cookies))
                .andExpect(status().isForbidden());

        Cookie[] withoutAccessToken = Arrays.stream(cookies)
                .filter(cookie -> !cookie.getName().equals("accessToken"))
                .toArray(Cookie[]::new);

        mockMvc.perform(get("/auth/test/user").cookie(withoutAccessToken))
                .andExpect(status().isOk()).andReturn();
    }

    @TestConfiguration
    @RequiredArgsConstructor
    public static class TestConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        @Primary
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/test/user").hasAuthority(AccountRole.USER.getKey())
                            .requestMatchers("/auth/test/admin").hasAuthority(AccountRole.ADMIN.getKey())
                            .anyRequest().permitAll()
                    )
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            return httpSecurity.build();
        }
    }

    @Testcontainers
    @TestConfiguration
    public static class RedisTestConfig {

        private static final int REDIS_PORT = 6379;

        @Container
        private static GenericContainer<?> redis = new GenericContainer<>("redis:8-alpine")
                .withExposedPorts(REDIS_PORT);

        static {
            redis.start();
        }

        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory(redis.getHost(), redis.getMappedPort(REDIS_PORT));
        }
    }
}
