package store.sonyk9919.api.domain.building.repository;

import static store.sonyk9919.api.domain.building.entity.QBuilding.building;
import static store.sonyk9919.api.domain.building.entity.QBuildingBase.buildingBase;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import store.sonyk9919.api.domain.building.entity.Building;


@Repository
@RequiredArgsConstructor
public class BuildingSearchRepository {
    private final JPAQueryFactory queryFactory;

    public Optional<Building> findByBuildingId(Long id) {
        Building result = queryFactory
                .selectFrom(building)
                .join(building.buildingBase, buildingBase).fetchJoin()
                .where(building.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public List<Building> findAll(Long islandId) {
        return queryFactory
                .selectFrom(building)
                .join(building.buildingBase, buildingBase).fetchJoin()
                .where(building.island.id.eq(islandId))
                .fetch();
    }
}
