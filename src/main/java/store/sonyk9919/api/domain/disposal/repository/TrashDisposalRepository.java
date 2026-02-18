package store.sonyk9919.api.domain.disposal.repository;

import org.springframework.data.repository.Repository;
import store.sonyk9919.api.domain.disposal.entity.TrashDisposal;

public interface TrashDisposalRepository extends Repository<TrashDisposal, Long> {

    int count();
}
