package store.sonyk9919.api.domain.island.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.shop.entity.ShopItem;
import store.sonyk9919.api.global.common.UriUtils;

@Component
@RequiredArgsConstructor
public class ItemModelUriResolver {

    private static final String TREE_PLANTING_CODE = "TREE_PLANTING";
    private static final String TRASH_SMALL_CODE   = "TRASH_REMOVAL_SMALL";
    private static final String TRASH_LARGE_CODE   = "TRASH_REMOVAL_LARGE";

    private final UriUtils uriUtils;

    public List<String> resolve(ShopItem item) {
        return switch (item.getCode()) {
            case TREE_PLANTING_CODE -> List.of(uriUtils.glbUri("tree_1"), uriUtils.glbUri("tree_2"));
            case TRASH_SMALL_CODE -> IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> uriUtils.glbUri("trash_small_" + i))
                    .collect(Collectors.toList());
            case TRASH_LARGE_CODE -> List.of(
                    uriUtils.glbUri("trash_large_1"), uriUtils.glbUri("trash_large_2"), uriUtils.glbUri("trash_large_3"));
            default -> List.of();
        };
    }
}
