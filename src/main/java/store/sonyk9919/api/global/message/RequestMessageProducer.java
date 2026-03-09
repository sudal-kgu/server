package store.sonyk9919.api.global.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.config.property.RabbitMQProperty;
import store.sonyk9919.api.global.message.exception.MQStatus;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestMessageProducer {
    private final RabbitMQProperty property;
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String requestId){
        try {
            log.info("[MQ] sent message: request_id: {}", requestId);
            rabbitTemplate.convertAndSend(
                    property.getExchange(),
                    property.getRequestRoutingKey(),
                    requestId
            );
        } catch (AmqpException e) {
            log.error("[MQ] failed to send message: request_id: {}", requestId, e);
            throw new CustomException(MQStatus.MQ_SEND_FAILED);
        }
    }
}