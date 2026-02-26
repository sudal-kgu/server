package store.sonyk9919.api.domain.trash.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.service.AnalysisRequestService;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.repository.TrashTaxonomyRepository;
import store.sonyk9919.api.domain.trash.dto.TrashResultDto;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.repository.TrashRepository;

@Service
@RequiredArgsConstructor
public class TrashService {
    private final TrashRepository trashRepository;
    private final TrashTaxonomyRepository taxonomyRepository;
    private final AnalysisRequestService analysisRequestService;
    private final TrashMapper trashMapper;

    @Transactional
    public TrashResultDto saveDetectedItems(String requestId, AnalysisResponseDto result) {
        List<DetectedItemDto> detectedItems = result.getDetectedItems();

        if (detectedItems == null || detectedItems.isEmpty()) {
            return TrashResultDto.of(requestId, Collections.emptyList());
        }

        AnalysisRequest analysisRequest = analysisRequestService.getByRequestId(requestId);
        Map<String, TrashTaxonomy> taxonomyMap = taxonomyRepository.findAllTaxonomy(detectedItems);

        List<Trash> trashes = trashMapper.toTrashes(analysisRequest, detectedItems, taxonomyMap);
        trashRepository.saveAll(trashes);

        return TrashResultDto.of(requestId, trashMapper.toTrashItems(trashes));
    }

    @Transactional(readOnly = true)
    public TrashResultDto fetchSavedItemsByRequestId(String requestId) {
        List<Trash> trashes = trashRepository.findByAnalysisRequest_RequestId(requestId);
        return TrashResultDto.of(requestId, trashMapper.toTrashItems(trashes));
    }

}