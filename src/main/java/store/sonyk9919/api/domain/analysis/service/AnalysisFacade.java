package store.sonyk9919.api.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.global.file.service.FileStorage;
import store.sonyk9919.api.global.message.RequestMessageProducer;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisFacade {
    private final AnalysisRequestService analysisRequestService;
    private final FileStorage fileStorage;
    private final RequestMessageProducer messageProducer;

    public String submitAnalysis(MultipartFile image) {
        String requestId = analysisRequestService.register();
        fileStorage.saveFile(image, requestId);

        messageProducer.sendMessage(requestId);

        return requestId;
    }
}
