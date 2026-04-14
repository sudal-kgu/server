package store.sonyk9919.api.global.common.advice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import store.sonyk9919.api.global.common.dto.BaseResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GlobalResponseAdviceTest {

    @InjectMocks private GlobalResponseAdvice advice;
    @Mock private MethodParameter methodParameter;

    @Test
    @DisplayName("이미 BaseResponse인 경우 그대로 반환한다")
    void alreadyBaseResponse() {
        BaseResponse<String> body = BaseResponse.success("hello");
        Object result = advice.beforeBodyWrite(body, methodParameter,
                MediaType.APPLICATION_JSON, JacksonJsonHttpMessageConverter.class,
                mock(ServerHttpRequest.class), mock(ServerHttpResponse.class));
        assertThat(result).isSameAs(body);
    }

    @Test
    @DisplayName("일반 객체인 경우 BaseResponse로 래핑하여 반환한다")
    void wrapNormalObject() {
        String body = "hello";
        Object result = advice.beforeBodyWrite(body, methodParameter,
                MediaType.APPLICATION_JSON, JacksonJsonHttpMessageConverter.class,
                mock(ServerHttpRequest.class), mock(ServerHttpResponse.class));
        assertThat(result).isInstanceOf(BaseResponse.class);
        BaseResponse<?> response = (BaseResponse<?>) result;
        assertThat(response.getData()).isEqualTo("hello");
    }

    @Test
    @DisplayName("Swagger(springdoc) 클래스는 지원하지 않는다")
    void supportsFalseForSwagger() {
        Class<?> swaggerClass = org.springdoc.api.AbstractOpenApiResource.class;
        given(methodParameter.getContainingClass()).willReturn((Class) swaggerClass);
        boolean result = advice.supports(methodParameter, JacksonJsonHttpMessageConverter.class);
        assertThat(result).isFalse();
    }
}