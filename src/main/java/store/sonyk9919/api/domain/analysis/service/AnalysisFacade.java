package store.sonyk9919.api.domain.analysis.service;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.domain.analysis.async.AnalysisAsyncTask;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.file.FileStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisFacade {
    private final AnalysisRequestService analysisRequestService;
    private final AnalysisAsyncTask analysisAsyncTask;
    private final AnalysisResultNotifier analysisResultNotifier;
    private final FileStorage fileStorage;

    public String submitAnalysis(MultipartFile image) {
        String requestId = analysisRequestService.register();
        fileStorage.saveFile(image, requestId);

        CompletableFuture<AnalysisResponseDto> future = analysisAsyncTask.runAnalysis(requestId);
        registerCallbacks(future, requestId);

        return requestId;
    }

    private void registerCallbacks(CompletableFuture<AnalysisResponseDto> future, String requestId) {
        future.thenAccept(result -> {
                onSuccess(requestId, result);
            }).exceptionally(ex -> {
                onFailure(requestId, ex);
                return null;
            });
    }

    private void onSuccess(String requestId, AnalysisResponseDto result) {
        analysisResultNotifier.saveAndNotify(requestId, result);
    }

    private void onFailure(String requestId, Throwable ex) {
        log.error("[Analysis] Async task failed. requestId={}", requestId, ex);
        analysisResultNotifier.notifyError(requestId, ErrorStatus.INTERNAL_SERVER_ERROR);
    }
}
