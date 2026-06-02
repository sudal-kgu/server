package store.sonyk9919.api.domain.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.shop.entity.GemItem;
import store.sonyk9919.api.domain.shop.entity.ShopItem;
import store.sonyk9919.api.global.common.UriUtils;

@Component
@RequiredArgsConstructor
public class IconUriResolver {

    private final UriUtils uriUtils;

    public String resolve(ShopItem item) {
        return uriUtils.shopIconUri(item.toFilename());
    }

    public String resolve(GemItem item) {
        return uriUtils.gemIconUri(item.toFilename());
    }
}
