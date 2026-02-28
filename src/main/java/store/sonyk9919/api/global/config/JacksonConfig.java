package store.sonyk9919.api.global.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.support.JacksonHandlerInstantiator;
import tools.jackson.databind.json.JsonMapper;

import static tools.jackson.databind.json.JsonMapper.*;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapper jsonMapper(Builder builder, ApplicationContext context) {
        JacksonHandlerInstantiator instantiator = new JacksonHandlerInstantiator(context.getAutowireCapableBeanFactory());
        return builder.handlerInstantiator(instantiator)
                .build();
    }
}
