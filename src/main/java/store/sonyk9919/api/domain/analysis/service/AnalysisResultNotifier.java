package store.sonyk9919.api.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.entity.AnalysisProgress;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.sse.service.SseResultSender;
import store.sonyk9919.api.domain.trash.dto.TrashResultDto;
import store.sonyk9919.api.domain.trash.service.TrashService;
import store.sonyk9919.api.global.common.dto.ErrorStatus;

@Service
@RequiredArgsConstructor
public class AnalysisResultNotifier {
    private final TrashService trashService;
    private final SseResultSender sseResultSender;
    private final AnalysisRequestService analysisRequestService;

    @Transactional
    public void saveAndNotify(String requestId, AnalysisResponseDto result) {
        TrashResultDto saved = trashService.saveDetectedItems(requestId, result);
        analysisRequestService.updateAnalysisProgress(requestId, AnalysisProgress.COMPLETED);
        sseResultSender.sendSuccess(requestId, saved);
    }

    public void notifyError(String requestId, ErrorStatus status) {
        analysisRequestService.updateAnalysisProgress(requestId, AnalysisProgress.FAILED);
        sseResultSender.sendError(requestId, status);
    }

    public void notifyIfAlreadyCompleted(String requestId) {
        AnalysisRequest request = analysisRequestService.getByRequestId(requestId);

        switch (request.getState()){
            case PENDING -> {return;}
            case FAILED -> {
                notifyError(requestId, ErrorStatus.INTERNAL_SERVER_ERROR);
                return;
            }
        }

        TrashResultDto saved = trashService.fetchSavedItemsByRequestId(requestId);
        sseResultSender.sendSuccess(requestId, saved);
    }
}
