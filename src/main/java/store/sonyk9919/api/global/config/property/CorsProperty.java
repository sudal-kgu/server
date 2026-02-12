package store.sonyk9919.api.global.config.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "security.cors")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CorsProperty {

    private final List<String> origins;
    private final List<String> methods;
    private final List<String> headers;
    private final String path;
    private final Long maxAge;
}
