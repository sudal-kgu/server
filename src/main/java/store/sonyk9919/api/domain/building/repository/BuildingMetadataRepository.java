package store.sonyk9919.api.domain.building.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;

public interface BuildingMetadataRepository extends JpaRepository<BuildingMetadata, Long> {

    @EntityGraph(attributePaths = {"yields"})
    List<BuildingMetadata> findAll();

    @Query("SELECT m FROM BuildingMetadata m JOIN FETCH m.yields y WHERE y.level = 1")
    List<BuildingMetadata> findAllWithLevelOneYield();
}
