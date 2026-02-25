package store.sonyk9919.api.domain.taxonomy.repository.custom;

import java.util.List;
import java.util.Map;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;

public interface TrashTaxonomyRepositoryCustom {
    Map<String, TrashTaxonomy> findAllByExactCategoryAndSubcategoryPairs(List<DetectedItemDto> detectedItems);
}