package store.sonyk9919.api.domain.shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.shop.entity.GemItem;
import store.sonyk9919.api.domain.shop.entity.ShopItem;

@Component
public class IconUriResolver {

    @Value("${image.base-url}")
    private String imageBaseUrl;

    public String resolve(ShopItem item) {
        return String.format("%s/shop-icons/%s.png", imageBaseUrl, toFilename(item.getCode()));
    }

    public String resolve(GemItem item) {
        return String.format("%s/gem-icons/%s.png", imageBaseUrl, toFilename(item.getCode()));
    }

    private String toFilename(String code) {
        return code.toLowerCase().replace('_', '-');
    }
}
