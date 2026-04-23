package store.sonyk9919.api.support;

import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import store.sonyk9919.api.domain.auth.service.KakaoProvider;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockitoBean protected RedissonClient redissonClient;

    @MockitoBean protected ConnectionFactory connectionFactory;

    @MockitoBean protected KakaoProvider kakaoProvider;
}
