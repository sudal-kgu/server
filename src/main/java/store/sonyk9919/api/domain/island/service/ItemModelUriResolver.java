package store.sonyk9919.api.domain.island.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.shop.entity.ShopItem;

@Component
public class ItemModelUriResolver {

    private static final String TREE_PLANTING_CODE = "TREE_PLANTING";
    private static final String TRASH_SMALL_CODE   = "TRASH_REMOVAL_SMALL";
    private static final String TRASH_LARGE_CODE   = "TRASH_REMOVAL_LARGE";

    @Value("${image.base-url}")
    private String imageBaseUrl;

    public List<String> resolve(ShopItem item) {
        return switch (item.getCode()) {
            case TREE_PLANTING_CODE -> List.of(modelUri("tree_1"), modelUri("tree_2"));
            case TRASH_SMALL_CODE -> IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> modelUri("trash_small_" + i))
                    .collect(Collectors.toList());
            case TRASH_LARGE_CODE -> List.of(
                    modelUri("trash_large_1"), modelUri("trash_large_2"), modelUri("trash_large_3"));
            default -> List.of();
        };
    }

    private String modelUri(String baseFilename) {
        return String.format("%s/models/%s.glb", imageBaseUrl, baseFilename);
    }
}
