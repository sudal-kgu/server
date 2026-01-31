package store.sonyk9919.api.domain.analysis.manager;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisCompletionHandler {

    public void registerCallbacks(CompletableFuture<Void> future, String requestId) {
        future.thenAccept(result -> {
            handleSuccess(requestId);
        }).exceptionally(ex -> {
            handleFailure(requestId, ex);
            return null;
        });
    }

    private void handleSuccess(String requestId) {
        log.info("분석 성공 -> requestId: {}", requestId);
    }

    private void handleFailure(String requestId, Throwable ex) {
        log.error("분석 에러 -> requestId: {}", requestId, ex);
    }
}