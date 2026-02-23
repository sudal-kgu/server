package store.sonyk9919.api.domain.taxonomy.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.repository.TrashTaxonomyRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrashTaxonomyService {
    private final TrashTaxonomyRepository taxonomyRepository;

    public Map<String, TrashTaxonomy> fetchTaxonomyMap(List<DetectedItemDto> detectedItems) {
        if (detectedItems == null || detectedItems.isEmpty()) {
            return Collections.emptyMap();
        }

        return taxonomyRepository.findAllByExactCategoryAndSubcategoryPairs(detectedItems)
                .stream()
                .collect(Collectors.toMap(
                        taxonomy ->
                                buildTaxonomyKey(
                                        taxonomy.getCategory().getName(),
                                        taxonomy.getSubCategory().getAlias()
                                ),
                        taxonomy -> taxonomy
                ));
    }

    public Optional<TrashTaxonomy> getTaxonomyFromMap(DetectedItemDto detectedItem, Map<String, TrashTaxonomy> taxonomyMap) {
        String key = buildTaxonomyKey(detectedItem.getCategory(), detectedItem.getSubcategory());
        TrashTaxonomy taxonomy = taxonomyMap.get(key);

        if (taxonomy == null) {
            log.warn("[Taxonomy] fail to mapping - category: {}, subcategory: {}",
                    detectedItem.getCategory(), detectedItem.getSubcategory());
            return Optional.empty();
        }

        return Optional.of(taxonomy);
    }

    public String buildTaxonomyKey(String category, String subcategory) {
        return category + ":" + subcategory;
    }
}