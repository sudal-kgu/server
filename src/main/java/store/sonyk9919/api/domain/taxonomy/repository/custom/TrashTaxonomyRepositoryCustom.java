package store.sonyk9919.api.domain.taxonomy.repository.custom;

import java.util.List;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;

public interface TrashTaxonomyRepositoryCustom {
    List<TrashTaxonomy> findAllByExactCategoryAndSubcategoryPairs(List<DetectedItemDto> detectedItems);
}