package store.sonyk9919.api.domain.building.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingEffect;
import store.sonyk9919.api.domain.building.entity.EffectType;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;

@Component
@RequiredArgsConstructor
public class IslandBoostCache {

    private final SlotRepository slotRepository;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "islandBoost", key = "#island.id")
    public double getTotalBoost(MemberIsland island) {
        return calculateTotalEffect(island, EffectType.ISLAND_BOOST);
    }

    @Cacheable(cacheNames = "islandQuizRewardBoost", key = "#island.id")
    public double getQuizRewardBoost(MemberIsland island) {
        return calculateTotalEffect(island, EffectType.QUIZ_REWARD_BOOST);
    }

    @Cacheable(cacheNames = "islandQuizRewardAdd", key = "#island.id")
    public double getQuizRewardAdd(MemberIsland island) {
        return calculateTotalEffect(island, EffectType.QUIZ_REWARD_ADD);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBuildingChanged(MemberIsland island) {
        evictBoostCache(island);
    }

    private double calculateTotalEffect(MemberIsland island, EffectType targetType) {
        return slotRepository.findAllByIsland(island).stream()
                .filter(Objects::nonNull)
                .filter(Slot::hasBuilding)
                .map(s -> {
                    Building building = s.getBuilding();
                    return building.getBuildingMetadata().getYieldForLevel(building.getCurrentLevel()).getEffect();
                })
                .filter(Objects::nonNull)
                .filter(effect -> effect.isEquals(targetType))
                .mapToDouble(BuildingEffect::getEffectValue)
                .sum();
    }

    public void evictBoostCache(MemberIsland island) {
        Long islandId = island.getId();
        evictCacheIfPresent("islandBoost", islandId);
        evictCacheIfPresent("islandQuizRewardBoost", islandId);
        evictCacheIfPresent("islandQuizRewardAdd", islandId);
    }

    private void evictCacheIfPresent(String cacheName, Long key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evictIfPresent(key);
        }
    }
}