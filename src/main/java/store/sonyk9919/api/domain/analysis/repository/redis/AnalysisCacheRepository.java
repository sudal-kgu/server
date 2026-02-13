package store.sonyk9919.api.domain.analysis.repository.redis;

import org.springframework.data.repository.CrudRepository;
import store.sonyk9919.api.domain.analysis.entity.AnalysisCache;

public interface AnalysisCacheRepository extends CrudRepository<AnalysisCache, String> {
}
