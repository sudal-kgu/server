package store.sonyk9919.api.global.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.exception.AnalysisStatus;
import store.sonyk9919.api.domain.analysis.service.AnalysisResultNotifier;
import store.sonyk9919.api.global.common.dto.ErrorStatus;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResultMessageConsumer {
    private final AnalysisResultNotifier analysisResultNotifier;

    @RabbitListener(queues = "${rabbitmq.result-queue-name}")
    public void consume(AnalysisResponseDto analysisResponseDto){
        log.info("[MQ] received result: requestId={}", analysisResponseDto.getRequestId());

        if (!analysisResponseDto.isSuccess()) {
            String error = analysisResponseDto.getError();

            log.error("[FastAPI] failed to analysis, requestId={}: error={}", analysisResponseDto.getRequestId(), error);

            analysisResultNotifier.notifyError(
                    analysisResponseDto.getRequestId(),
                    AnalysisStatus.fromFastApiError(error)
            );
            return;
        }

        try {
            analysisResultNotifier.saveAndNotify(analysisResponseDto);
        } catch (Exception e) {
            log.error("[MQ] failed to process result: requestId={}", analysisResponseDto.getRequestId(), e);
            analysisResultNotifier.notifyError(
                    analysisResponseDto.getRequestId(),
                    ErrorStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}