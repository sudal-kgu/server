package store.sonyk9919.api.domain.analysis.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;

public interface AnalysisRequestRepository extends JpaRepository<AnalysisRequest, Long> {
    Optional<AnalysisRequest> findByRequestId(String requestId);
}
