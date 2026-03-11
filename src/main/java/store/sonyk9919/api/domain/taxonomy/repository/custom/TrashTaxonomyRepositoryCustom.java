package store.sonyk9919.api.domain.taxonomy.repository.custom;

import java.util.List;
import java.util.Map;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.taxonomy.entity.TrashTaxonomy;

public interface TrashTaxonomyRepositoryCustom {
    Map<String, TrashTaxonomy> findAllTaxonomy(List<DetectedItemDto> detectedItems);
}