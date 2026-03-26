package store.sonyk9919.api.domain.trash.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import store.sonyk9919.api.global.file.serializer.ImageSerializer;
import tools.jackson.databind.annotation.JsonSerialize;

import java.nio.file.Paths;
import java.util.*;

@Getter
public class TrashDetailDto {
    private static final String DISPOSAL_CATEGORY_NAME = "category";
    private static final String DISPOSAL_SUB_CATEGORY_NAME = "subcategory";

    private final String category;
    private final String subcategory;
    @JsonSerialize(using = ImageSerializer.class) private final String image;
    private final Map<String, List<String>> disposal;

    @QueryProjection
    public TrashDetailDto(
            String category,
            String subcategory,
            String requestId,
            String filename,
            Set<String> disposalCategory,
            Set<String> disposalSubCategory
    ) {
        this.category = category;
        this.subcategory = subcategory;
        this.image = Paths.get(requestId, filename).toString();
        this.disposal = new HashMap<>();
        initDisposal(disposal, disposalCategory, DISPOSAL_CATEGORY_NAME);
        initDisposal(disposal, disposalSubCategory, DISPOSAL_SUB_CATEGORY_NAME);
    }

    private void initDisposal(Map<String, List<String>> map, Collection<String> disposal, String name) {
        if (disposal == null || disposal.isEmpty()) return;
        List<String> steps = disposal.stream()
                .filter(Objects::nonNull)
                .flatMap(d -> Arrays.stream(d.split("\\r?\\n")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        map.put(name, steps);
    }
}
