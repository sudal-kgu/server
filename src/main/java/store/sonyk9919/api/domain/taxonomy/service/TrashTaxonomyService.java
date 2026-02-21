package store.sonyk9919.api.domain.taxonomy.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto.DetectedItem;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.repository.TrashTaxonomyRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrashTaxonomyService {
    private final TrashTaxonomyRepository taxonomyRepository;

    public Map<String, TrashTaxonomy> getTaxonomyMap(List<DetectedItem> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyMap();
        }

        List<String> keys = items.stream()
                .map(item -> taxonomyKey(item.getCategory(), item.getSubcategory()))
                .distinct()
                .collect(Collectors.toList());

        return taxonomyRepository.findAllByExactCategoryAndSubcategoryPairs(keys)
                .stream()
                .collect(Collectors.toMap(
                        taxonomy -> taxonomyKey(taxonomy.getCategory().getName(), taxonomy.getSubCategory().getAlias()),
                        taxonomy -> taxonomy
                ));
    }

    public TrashTaxonomy findTaxonomy(DetectedItem item, Map<String, TrashTaxonomy> taxonomyMap) {
        String key = taxonomyKey(item.getCategory(), item.getSubcategory());
        TrashTaxonomy taxonomy = taxonomyMap.get(key);

        if (taxonomy == null) {
            log.warn("[Taxonomy] fail to mapping. key: {}", key);
            return null;
        }
        return taxonomy;
    }

    private String taxonomyKey(String category, String subcategory) {
        return category + ":" + subcategory;
    }
}