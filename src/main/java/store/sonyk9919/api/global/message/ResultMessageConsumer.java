package store.sonyk9919.api.global.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.exception.AnalysisStatus;
import store.sonyk9919.api.domain.analysis.service.AnalysisResultNotifier;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResultMessageConsumer {
    private final AnalysisResultNotifier analysisResultNotifier;

    @RabbitListener(queues = "${rabbitmq.result-queue-name}")
    public void consume(AnalysisResponseDto analysisResponseDto){
        log.info("[MQ] Received result: requestId={}", analysisResponseDto.getRequestId());

        if (!analysisResponseDto.isSuccess()) {
            String error = analysisResponseDto.getError();

            log.error("[FastAPI] analysis failed requestId={}: error={}", analysisResponseDto.getRequestId(), error);

            analysisResultNotifier.notifyError(
                    analysisResponseDto.getRequestId(),
                    AnalysisStatus.fromFastApiError(error)
            );
            return;
        }
        analysisResultNotifier.saveAndNotify(analysisResponseDto);
    }
}