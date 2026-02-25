package store.sonyk9919.api.domain.trash.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;
import store.sonyk9919.api.domain.trash.dto.TrashItemDto;
import store.sonyk9919.api.domain.trash.entity.Trash;

@Slf4j
@Component
public class TrashMapper {
    public List<Trash> toTrashes(AnalysisRequest analysisRequest, List<DetectedItemDto> detectedItems, Map<String, TrashTaxonomy> taxonomyMap) {
        return detectedItems.stream()
                .filter(item -> hasTaxonomy(item, taxonomyMap))
                .map(item -> Trash.create(analysisRequest, taxonomyMap.get(item.getKey()), item.getFilename()))
                .collect(Collectors.toList());
    }

    public List<TrashItemDto> toTrashItems(List<Trash> trashes) {
        return trashes.stream()
                .map(TrashItemDto::from)
                .collect(Collectors.toList());
    }

    private boolean hasTaxonomy(DetectedItemDto detectedItem, Map<String, TrashTaxonomy> taxonomyMap) {
        boolean exists = taxonomyMap.containsKey(detectedItem.getKey());
        if (!exists) {
            log.warn("[Taxonomy] fail to mapping - category: {}, subcategory: {}",
                    detectedItem.getCategory(),
                    detectedItem.getSubcategory());
        }
        return exists;
    }
}
