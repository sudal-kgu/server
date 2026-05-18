package store.sonyk9919.api.domain.island.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.service.IslandBoostCache;
import store.sonyk9919.api.domain.island.dto.IslandEffectDto;
import store.sonyk9919.api.domain.island.dto.MemberIslandDto;
import store.sonyk9919.api.domain.island.dto.NextLevelConditionDto;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.member.service.MemberAccountService;
import store.sonyk9919.api.domain.slot.service.SlotSetupService;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberIslandRegistryService {

    private final MemberIslandRepository memberIslandRepository;
    private final MemberAccountService memberAccountService;
    private final SlotSetupService slotSetupService;
    private final ResourceSetupService resourceSetupService;
    private final LevelSpecCache levelSpecCache;
    private final IslandBoostCache islandBoostCache;

    @Transactional
    public MemberIsland create(Long memberAccountId, String nickname) {
        return createEntity(memberAccountId, nickname);
    }

    @Transactional
    public MemberIslandDto createDto(Long memberAccountId, String nickname) {
        MemberIsland island = createEntity(memberAccountId, nickname);
        return MemberIslandDto.from(island, buildNextLevelCondition(island));
    }

    private MemberIsland createEntity(Long memberAccountId, String nickname) {
        MemberAccount account = memberAccountService.getMemberAccount(memberAccountId);
        MemberIsland island = MemberIsland.create(nickname, account);
        memberIslandRepository.save(island);
        slotSetupService.setupSlots(island);
        resourceSetupService.setupResources(island);
        return island;
    }

    public MemberIsland getIsland(Long memberAccountId) {
        return memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
    }

    public MemberIslandDto getIslandDto(Long memberAccountId) {
        MemberIsland island = memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
        return MemberIslandDto.from(island, buildNextLevelCondition(island), createIslandEffect(island));
    }

    private NextLevelConditionDto buildNextLevelCondition(MemberIsland island) {
        if (island.isMaxLevel()) return null;
        LevelSpec currentSpec = levelSpecCache.get(island.getLevel());
        return NextLevelConditionDto.from(currentSpec);
    }

    private IslandEffectDto createIslandEffect(MemberIsland island){
        return IslandEffectDto.of(
                islandBoostCache.getTotalBoost(island),
                islandBoostCache.getQuizRewardBoost(island),
                islandBoostCache.getQuizRewardAdd(island)
        );
    }
}
