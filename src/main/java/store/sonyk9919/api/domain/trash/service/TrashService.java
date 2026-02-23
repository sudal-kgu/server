package store.sonyk9919.api.domain.trash.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.service.AnalysisRequestService;
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
        List<DetectedItemDto> detectedItems = result.getDetectedItems();

        if (detectedItems == null || detectedItems.isEmpty()) {
            return TrashResultDto.of(requestId, Collections.emptyList());
        }

        AnalysisRequest analysisRequest = analysisRequestService.getByRequestId(requestId);
        Map<String, TrashTaxonomy> taxonomyMap = trashTaxonomyService.fetchTaxonomyMap(detectedItems);

        List<Trash> trashes = detectedItems.stream()
                .flatMap(item -> trashTaxonomyService.getTaxonomyFromMap(item, taxonomyMap)
                        .map(taxonomy -> Trash.create(analysisRequest, taxonomy, item.getFilename()))
                        .stream())
                .collect(Collectors.toList());
        trashRepository.saveAll(trashes);

        return TrashResultDto.of(requestId, toTrashItems(trashes));
    }

    @Transactional(readOnly = true)
    public TrashResultDto fetchSavedItemsByRequestId(String requestId) {
        List<Trash> trashes = trashRepository.findByAnalysisRequest_RequestId(requestId);
        return TrashResultDto.of(requestId, toTrashItems(trashes));
    }

    private List<TrashItemDto> toTrashItems(List<Trash> trashes) {
        return trashes.stream()
                .map(TrashItemDto::from)
                .collect(Collectors.toList());
    }
}