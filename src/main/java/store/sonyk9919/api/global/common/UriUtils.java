package store.sonyk9919.api.global.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UriUtils {

    @Value("${image.base-url}")
    private String imageBaseUrl;

    public String glbUri(String baseFilename) {
        return String.format("%s/models/%s.glb", imageBaseUrl, baseFilename);
    }

    public String shopIconUri(String filename) {
        return String.format("%s/shop-icons/%s.png", imageBaseUrl, filename);
    }

    public String gemIconUri(String filename) {
        return String.format("%s/gem-icons/%s.png", imageBaseUrl, filename);
    }
}
