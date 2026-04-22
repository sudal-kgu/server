package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.building.entity.BuildingEffect;
import store.sonyk9919.api.domain.building.entity.EffectType;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;

@Component
@RequiredArgsConstructor
public class IslandBoostCache {

    private final SlotRepository slotRepository;

    @Cacheable(cacheNames = "islandBoost", key = "#island.id")
    public double getTotalBoost(MemberIsland island) {
        return slotRepository.findAllByIsland(island).stream()
                .filter(s -> s.getBuilding() != null)
                .map(s -> s.getBuilding().getBuildingMetadata().getYieldForLevel(s.getBuilding().getCurrentLevel()).getEffect())
                .filter(effect -> effect != null && effect.getType() == EffectType.ISLAND_BOOST)
                .mapToDouble(BuildingEffect::getEffectValue)
                .sum();
    }

    @CacheEvict(cacheNames = "islandBoost", key = "#island.id")
    public void evictBoostCache(MemberIsland island) { }
}