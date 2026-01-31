package store.sonyk9919.api.domain.analysis.service;

import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.repository.AnalysisRequestRepository;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final AnalysisRequestRepository requestRepository;
    private final AnalysisAsyncTask analysisAsyncTask;

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    @Transactional
    public String registerAnalysisRequest(MultipartFile image) {
        AnalysisRequest analysisRequest = AnalysisRequest.createWithUUID();
        requestRepository.save(analysisRequest);
        String requestId = analysisRequest.getRequestId();

        saveFile(image, requestId);

        analysisAsyncTask.runAnalysis(requestId);
        return requestId;
    }

    private void saveFile(MultipartFile image, String requestId) {
        try {
            File dest = new File(UPLOAD_DIR + requestId + ".jpg");
            image.transferTo(dest);
        } catch (IOException e) {
            log.error("[FileSave] Failed: {}", requestId, e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }
}