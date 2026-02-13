package store.sonyk9919.api.domain.analysis.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.analysis.entity.AnalysisResult;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {
}
