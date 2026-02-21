package store.sonyk9919.api.domain.trash.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto.DetectedItem;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.service.AnalysisRequestService;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashCategory;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashSubCategory;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.service.TrashTaxonomyService;
import store.sonyk9919.api.domain.trash.dto.TrashItemDto;
import store.sonyk9919.api.domain.trash.dto.TrashResultDto;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.repository.TrashRepository;

@Service
@RequiredArgsConstructor
public class TrashService {
    private final TrashRepository trashRepository;
    private final TrashTaxonomyService trashTaxonomyService;
    private final AnalysisRequestService analysisRequestService;

    @Transactional
    public TrashResultDto saveDetectedItems(String requestId, AnalysisResponseDto result) {
        List<DetectedItem> detectedItems = result.getDetectedItems();

        if (detectedItems == null || detectedItems.isEmpty()) {
            return TrashResultDto.of(requestId, Collections.emptyList());
        }

        AnalysisRequest analysisRequest = analysisRequestService.getByRequestId(requestId);
        Map<String, TrashTaxonomy> taxonomyMap = trashTaxonomyService.getTaxonomyMap(detectedItems);

        List<Trash> trashes = detectedItems.stream()
                .map(item -> convertToTrash(analysisRequest, item, taxonomyMap))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        trashRepository.saveAll(trashes);

        return TrashResultDto.of(requestId, toResultItems(trashes));
    }

    @Transactional(readOnly = true)
    public TrashResultDto getSavedItemsByRequestId(String requestId) {
        List<Trash> trashes = trashRepository.findByAnalysisRequest_RequestId(requestId);
        return TrashResultDto.of(requestId, toResultItems(trashes));
    }

    private List<TrashItemDto> toResultItems(List<Trash> trashes) {
        return trashes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private Trash convertToTrash(AnalysisRequest request, DetectedItem item, Map<String, TrashTaxonomy> taxonomyMap) {
        TrashTaxonomy taxonomy = trashTaxonomyService.findTaxonomy(item, taxonomyMap);

        if (taxonomy == null) {
            return null;
        }

        return Trash.create(request, taxonomy, item.getFilename());
    }

    private TrashItemDto convertToDto(Trash trash) {
        TrashTaxonomy taxonomy = trash.getTaxonomy();
        TrashCategory category = taxonomy.getCategory();
        TrashSubCategory subCategory = taxonomy.getSubCategory();

        return TrashItemDto.of(
                trash.getTrashId(),
                trash.getFilename(),
                category.getName(),
                subCategory.getAlias()
        );
    }
}