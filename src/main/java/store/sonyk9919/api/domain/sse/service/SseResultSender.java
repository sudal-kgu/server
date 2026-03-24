package store.sonyk9919.api.domain.sse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.sse.type.SseEventType;
import store.sonyk9919.api.domain.trash.dto.TrashResultDto;
import store.sonyk9919.api.global.common.dto.BaseResponse;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Component
@RequiredArgsConstructor
public class SseResultSender {
    private final SseEmitterService sseEmitterService;

    public void sendSuccess(String requestId, TrashResultDto data) {
        sseEmitterService.sendAndComplete(
                requestId,
                SseEventType.ANALYSIS_RESULT,
                BaseResponse.success(data)
        );
    }

    public void sendError(String requestId, BaseResponseStatus status) {
        sseEmitterService.sendAndComplete(
                requestId,
                SseEventType.ERROR,
                BaseResponse.error(status).getBody()
        );
    }
}
