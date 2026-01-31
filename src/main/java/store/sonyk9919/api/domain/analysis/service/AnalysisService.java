package store.sonyk9919.api.domain.analysis.service;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.manager.AnalysisAsyncTask;
import store.sonyk9919.api.domain.analysis.manager.AnalysisCompletionHandler;
import store.sonyk9919.api.global.file.FileStorage;
import store.sonyk9919.api.domain.analysis.repository.AnalysisRequestRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final AnalysisRequestRepository requestRepository;
    private final AnalysisAsyncTask analysisAsyncTask;
    private final FileStorage fileStorage;
    private final AnalysisCompletionHandler completionHandler;

    @Transactional
    public String submitAnalysis(MultipartFile image) {
        AnalysisRequest analysisRequest = AnalysisRequest.createWithUUID();
        requestRepository.save(analysisRequest);

        String requestId = analysisRequest.getRequestId();
        fileStorage.saveFile(image, requestId);

        CompletableFuture<Void> future = analysisAsyncTask.runAnalysis(requestId);
        completionHandler.registerCallbacks(future, requestId);

        return requestId;
    }
}