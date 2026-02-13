package store.sonyk9919.api.global.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "security.cookie")
public class CookieProperty {

    private final String sameSite;
    private final String domain;
}
