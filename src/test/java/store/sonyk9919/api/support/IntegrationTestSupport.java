package store.sonyk9919.api.support;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockitoBean protected ConnectionFactory connectionFactory;
}
