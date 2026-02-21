package store.sonyk9919.api.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.sse.service.SseResultSender;
import store.sonyk9919.api.domain.trash.dto.TrashResultDto;
import store.sonyk9919.api.domain.trash.service.TrashService;
import store.sonyk9919.api.global.common.dto.ErrorStatus;

@Service
@RequiredArgsConstructor
public class AnalysisResultNotifier {
    private final TrashService trashService;
    private final SseResultSender sseResultSender;

    public void saveAndNotify(String requestId, AnalysisResponseDto result) {
        TrashResultDto saved = trashService.saveDetectedItems(requestId, result);
        sseResultSender.sendSuccess(requestId, saved);
    }

    public void notifyError(String requestId, ErrorStatus status) {
        sseResultSender.sendError(requestId, status);
    }

    public void notifyIfAlreadyCompleted(String requestId) {
        TrashResultDto saved = trashService.getSavedItemsByRequestId(requestId);
        if (saved.isEmptyItems()) return;

        sseResultSender.sendSuccess(requestId, saved);
    }
}
