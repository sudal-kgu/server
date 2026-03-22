package store.sonyk9919.api.global.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.service.AnalysisResultNotifier;
import store.sonyk9919.api.global.config.property.RabbitMQProperty;

@SpringBootTest
@Testcontainers
class RabbitMQIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQProperty property;

    @MockitoBean
    private AnalysisResultNotifier analysisResultNotifier;

    @Container
    private static final GenericContainer<?> rabbitMQ =
            new GenericContainer<>("rabbitmq:3-management-alpine")
                    .withExposedPorts(5672);
    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQ::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitMQ.getMappedPort(5672));
        registry.add("spring.rabbitmq.username", () -> "guest");
        registry.add("spring.rabbitmq.password", () -> "guest");
    }

    @Test
    @DisplayName("Request ID를 요청 큐에 전송")
    void sendRequestId() {
        String requestId = "request-001";

        rabbitTemplate.convertAndSend(
                property.getExchange(),
                property.getRequestRoutingKey(),
                requestId
        );

        String receivedMessage = (String) rabbitTemplate
                .receiveAndConvert(property.getRequestQueueName(), 3000);

        assertThat(receivedMessage).isEqualTo(requestId);
    }

    @Test
    @DisplayName("결과 큐에 메시지가 도착 시 saveAndNotify 호출 확인")
    void listeningResultQueue() throws Exception{
        String requestId = "request-002";

        String jsonPayload = """
                {
                    "request_id": "%s",
                    "count": 3,
                    "detected_items": []
                }
                """.formatted(requestId);

        AnalysisResponseDto responseDto = objectMapper.readValue(
                jsonPayload,
                AnalysisResponseDto.class
        );

        rabbitTemplate.convertAndSend(
                property.getExchange(),
                property.getResultRoutingKey(),
                responseDto
        );

        verify(analysisResultNotifier, timeout(3000))
                .saveAndNotify(argThat(dto -> dto.getRequestId().equals(requestId)));
    }

    @TestConfiguration
    @EnableRabbit
    static class RabbitMQTestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}