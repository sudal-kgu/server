package store.sonyk9919.api.domain.analysis.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.domain.analysis.async.AnalysisAsyncTask;
import store.sonyk9919.api.domain.analysis.async.AnalysisCompletionHandler;
import store.sonyk9919.api.domain.analysis.dto.DetectedItem;
import store.sonyk9919.api.domain.analysis.dto.request.AnalysisRequestDto;
import store.sonyk9919.api.domain.analysis.dto.response.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.dto.response.AnalysisSelectionDto;
import store.sonyk9919.api.domain.analysis.dto.response.AnalysisSelectionDto.MappedItem;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.entity.AnalysisResult;
import store.sonyk9919.api.domain.analysis.exception.AnalysisStatus;
import store.sonyk9919.api.domain.analysis.repository.jpa.AnalysisRequestRepository;
import store.sonyk9919.api.domain.analysis.repository.jpa.AnalysisResultRepository;
import store.sonyk9919.api.domain.trash.dto.TrashCreateDto;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.service.TrashService;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.file.FileStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final AnalysisRequestRepository requestRepository;
    private final AnalysisAsyncTask analysisAsyncTask;
    private final FileStorage fileStorage;
    private final AnalysisCompletionHandler completionHandler;

    private final AnalysisResultRepository resultRepository;
    private final TrashService trashService;

    @Transactional
    public String submitAnalysis(MultipartFile image) {
        AnalysisRequest analysisRequest = AnalysisRequest.createWithUUID();
        requestRepository.save(analysisRequest);

        String requestId = analysisRequest.getRequestId();
        fileStorage.saveFile(image, requestId);

        CompletableFuture<AnalysisResponseDto> future = analysisAsyncTask.runAnalysis(requestId);
        completionHandler.registerCallbacks(future, requestId);

        return requestId;
    }

    @Transactional
    public AnalysisSelectionDto confirmSelection(AnalysisRequestDto requestDto) {
        fileStorage.deleteUnSelectFiles(requestDto);

        AnalysisRequest analysisRequest = requestRepository.findByRequestId(requestDto.getRequestId())
                .orElseThrow(() -> new CustomException(AnalysisStatus.ANALYSIS_REQUEST_NOT_FOUND));

        AnalysisResult analysisResult = resultRepository.save(AnalysisResult.create());

        List<MappedItem> mappedItems = createMappedItems(requestDto.getDetectedItems(), analysisRequest, analysisResult);
        return AnalysisSelectionDto.of(requestDto.getRequestId(), mappedItems);
    }

    private List<MappedItem> createMappedItems(List<DetectedItem> detectedItems, AnalysisRequest request, AnalysisResult result) {
        return detectedItems.stream()
                .map(item -> saveAsTrash(item, request, result))
                .collect(Collectors.toList());
    }

    private MappedItem saveAsTrash(DetectedItem item, AnalysisRequest request, AnalysisResult result) {
        TrashCreateDto trashCreateDto = TrashCreateDto.create(
                request,
                result,
                item.getCategory(),
                item.getSubcategory(),
                item.getFilename()
        );

        Trash trash = trashService.saveTrash(trashCreateDto);
        return new MappedItem(trash.getId(), item.getFilename(), item.getCategory(), item.getSubcategory());
    }
}