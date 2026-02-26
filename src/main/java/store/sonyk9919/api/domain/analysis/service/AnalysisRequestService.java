package store.sonyk9919.api.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.analysis.entity.AnalysisProgress;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.exception.AnalysisStatus;
import store.sonyk9919.api.domain.analysis.repository.AnalysisRequestRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
public class AnalysisRequestService {
    private final AnalysisRequestRepository requestRepository;

    @Transactional
    public String register() {
        AnalysisRequest request = AnalysisRequest.createWithUUID();
        requestRepository.save(request);

        return request.getRequestId();
    }

    @Transactional
    public void updateAnalysisProgress(String requestId, AnalysisProgress progress) {
        AnalysisRequest request = getByRequestId(requestId);
        request.updateState(progress);
    }

    public AnalysisRequest getByRequestId(String requestId) {
        return requestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new CustomException(AnalysisStatus.ANALYSIS_REQUEST_NOT_FOUND));
    }
}
