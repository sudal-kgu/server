package store.sonyk9919.api.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.sonyk9919.api.global.config.property.RabbitMQProperty;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final RabbitMQProperty property;

    @Bean
    public Queue requestQueue(){
        return new Queue(property.getRequestQueueName());
    }

    @Bean
    public Queue resultQueue(){
        return new Queue(property.getResultQueueName());
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(property.getExchange());
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
    public MessageConverter converter(){
        return new JacksonJsonMessageConverter();
    }
}