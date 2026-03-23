package store.sonyk9919.api.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.sonyk9919.api.global.config.property.RabbitMQProperty;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final RabbitMQProperty property;
    public static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    public static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    @Bean
    public Queue requestQueue(){
        return new Queue(property.getRequestQueueName());
    }

    @Bean
    public Queue resultQueue(){
        return QueueBuilder.durable(property.getResultQueueName())
                .withArgument(X_DEAD_LETTER_EXCHANGE, property.getDeadLetterExchange())
                .withArgument(X_DEAD_LETTER_ROUTING_KEY, property.getDeadLetterRoutingKey())
                .build();
    }

    @Bean
    public Queue deadLetterQueue(){
        return new Queue(property.getDeadLetterQueueName());
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(property.getExchange());
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(property.getDeadLetterExchange());
    }

    @Bean
    public Binding requestBinding(){
        return BindingBuilder
                .bind(requestQueue())
                .to(exchange())
                .with(property.getRequestRoutingKey());
    }

    @Bean
    public Binding resultBinding(){
        return BindingBuilder
                .bind(resultQueue())
                .to(exchange())
                .with(property.getResultRoutingKey());
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(property.getDeadLetterRoutingKey());
    }

    @Bean
    public MessageConverter converter(){
        return new JacksonJsonMessageConverter();
    }
}