package store.sonyk9919.api.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.building.service.IslandBoostCache;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

@Component
@RequiredArgsConstructor
public class RecyclingRewardCalculator {

    private static final int BASE_SHELL = 10;

    private final IslandBoostCache islandBoostCache;

    public int calculateShell(MemberIsland island) {
        double addBonus = islandBoostCache.getQuizRewardAdd(island);
        double boostPercent = islandBoostCache.getQuizRewardBoost(island);

        return (int) ((BASE_SHELL + addBonus) * (1.0 + boostPercent));
    }
}
