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
        return imageBaseUrl + "/shop-icons/" + toFilename(item.getCode()) + ".png";
    }

    public String resolve(GemItem item) {
        return imageBaseUrl + "/gem-icons/" + toFilename(item.getCode()) + ".png";
    }

    private String toFilename(String code) {
        return code.toLowerCase().replace('_', '-');
    }
}
