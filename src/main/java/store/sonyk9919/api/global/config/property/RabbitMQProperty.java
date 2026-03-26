package store.sonyk9919.api.global.config.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "rabbitmq")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RabbitMQProperty {
    private final String requestQueueName;
    private final String resultQueueName;
    private final String exchange;
    private final String requestRoutingKey;
    private final String resultRoutingKey;
}