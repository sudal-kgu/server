package store.sonyk9919.api.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springBootAPI() {

        Info info = new Info()
                .title("untitled (KGU) API Documentation") // 팀명 확정 시 변경
                .description("심화 캡스톤: AI 분리수거 사전 서버 API 문서입니다.")
                .contact(new io.swagger.v3.oas.models.info.Contact()
                        .name("Server Repository")
                        .url("https://github.com/untitled-KGU/server"))
                .version("1.0.0");


        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info);
    }
}
