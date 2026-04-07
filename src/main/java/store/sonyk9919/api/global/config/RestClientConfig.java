package store.sonyk9919.api.global.config;

import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import store.sonyk9919.api.domain.auth.entity.KakaoApiClient;
import store.sonyk9919.api.domain.auth.entity.KakaoAuthClient;

@Configuration
public class RestClientConfig {
    @Value("${analysis.fastApi.url}")
    private String fastAPIUrl;

    @Value("${kakao.auth}")
    private String kakaoAuthUrl;

    @Value("${kakao.api}")
    private String kakaoApiUrl;

    @Bean
    public RestClient fastApiClient() {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(fastAPIUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public KakaoAuthClient kakaoAuthClient() {
        RestClient kAuthClient = RestClient.builder()
                .baseUrl(kakaoAuthUrl)
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(kAuthClient);
        return HttpServiceProxyFactory.builderFor(adapter)
                .build()
                .createClient(KakaoAuthClient.class);
    }

    @Bean
    public KakaoApiClient kakaoApiClient() {
        RestClient kApiClient = RestClient.builder()
                .baseUrl(kakaoApiUrl)
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(kApiClient);
        return HttpServiceProxyFactory.builderFor(adapter)
                .build()
                .createClient(KakaoApiClient.class);
    }
}
