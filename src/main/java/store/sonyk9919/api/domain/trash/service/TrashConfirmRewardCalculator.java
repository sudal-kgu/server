package store.sonyk9919.api.domain.trash.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.building.service.IslandBoostCache;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

@Component
@RequiredArgsConstructor
public class TrashConfirmRewardCalculator {

    private static final int BASE_DISPOSAL_SHELL = 10;

    private final IslandBoostCache islandBoostCache;

    public int calculateDisposalShell(MemberIsland island) {
        double addBonus = islandBoostCache.getDisposalRewardAdd(island);
        return (int) (BASE_DISPOSAL_SHELL + addBonus);
    }
}
