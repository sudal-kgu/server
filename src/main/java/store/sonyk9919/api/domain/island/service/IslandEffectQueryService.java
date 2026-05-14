package store.sonyk9919.api.domain.island.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.service.IslandBoostCache;
import store.sonyk9919.api.domain.island.dto.IslandEffectResponseDto;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

@Service
@RequiredArgsConstructor
public class IslandEffectQueryService {

    private final MemberIslandRegistryService islandRegistryService;
    private final IslandBoostCache islandBoostCache;

    @Transactional(readOnly = true)
    public IslandEffectResponseDto getEffects(Long memberId) {
        MemberIsland island = islandRegistryService.getIsland(memberId);
        return IslandEffectResponseDto.of(
                islandBoostCache.getTotalBoost(island),
                islandBoostCache.getQuizRewardBoost(island),
                islandBoostCache.getQuizRewardAdd(island)
        );
    }
}
