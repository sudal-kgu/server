package store.sonyk9919.api.domain.trash.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.trash.entity.Trash;

public interface TrashRepository extends JpaRepository<Trash, Long> {

    @EntityGraph(attributePaths = {"taxonomy", "taxonomy.category", "taxonomy.subCategory"})
    List<Trash> findByAnalysisRequest_RequestId(String requestId);

    List<Trash> findAllByTrashUuidIn(List<String> trashUuids);
}
