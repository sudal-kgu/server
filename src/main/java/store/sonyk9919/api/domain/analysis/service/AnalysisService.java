package store.sonyk9919.api.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.manager.AnalysisAsyncTask;
import store.sonyk9919.api.domain.analysis.manager.FileStorageManager;
import store.sonyk9919.api.domain.analysis.repository.AnalysisRequestRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final AnalysisRequestRepository requestRepository;
    private final AnalysisAsyncTask analysisAsyncTask;
    private final FileStorageManager fileStorageManager;

    @Transactional
    public String submitAnalysis(MultipartFile image) {
        AnalysisRequest analysisRequest = AnalysisRequest.createWithUUID();
        requestRepository.save(analysisRequest);

        String requestId = analysisRequest.getRequestId();
        fileStorageManager.saveFile(image, requestId);

        analysisAsyncTask.runAnalysis(requestId);
        return requestId;
    }
}