package store.sonyk9919.api.domain.building.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingEffect;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;

@Component
@RequiredArgsConstructor
public class IslandBoostCache {

    private final SlotRepository slotRepository;

    @Cacheable(cacheNames = "islandBoost", key = "#island.id")
    public double getTotalBoost(MemberIsland island) {
        return slotRepository.findAllByIsland(island).stream()
                .filter(Objects::nonNull)
                .filter(s -> s.getBuilding() != null)
                .map(s -> {
                    Building building = s.getBuilding();
                    BuildingMetadata metadata = building.getBuildingMetadata();
                    int currentLevel = building.getCurrentLevel();
                    return metadata.getYieldForLevel(currentLevel).getEffect();
                })
                .filter(Objects::nonNull)
                .filter(BuildingEffect::isIslandBoost)
                .mapToDouble(BuildingEffect::getEffectValue)
                .sum();
    }

    @CacheEvict(cacheNames = "islandBoost", key = "#island.id")
    public void evictBoostCache(MemberIsland island) { }
}