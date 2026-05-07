package store.sonyk9919.api.domain.building.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.building.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}
