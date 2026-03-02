package store.sonyk9919.api.global.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.global.config.property.RabbitMQProperty;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestMessageProducer {
    private final RabbitMQProperty property;
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String requestId){
        log.info("[MQ] sent Message: request_id: {}", requestId);
        rabbitTemplate.convertAndSend(
                property.getExchange(),
                property.getRequestRoutingKey(),
                requestId
        );
    }
}