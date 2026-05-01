package store.sonyk9919.api.domain.island.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.event.IslandLevelUpEvent;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IslandLevelService {

    private final MemberIslandRepository memberIslandRepository;
    private final LevelSpecCache levelSpecCache;
    private final ApplicationEventPublisher eventPublisher;

    @DistributedLock(key = "'island:' + #memberAccountId + ':exp'")
    @Transactional
    public void addRecyclingExp(Long memberAccountId) {
        MemberIsland island = getIsland(memberAccountId);
        LevelSpec currentSpec = getLevelSpec(island.getLevel());
        island.addRecyclingExp(currentSpec);
        checkAndProcessLevelUp(island);
    }

    @DistributedLock(key = "'island:' + #island.memberAccount.id + ':exp'")
    @Transactional
    public void addItemExp(MemberIsland island, int expAmount) {
        island.addItemExp(expAmount);
        checkAndProcessLevelUp(island);
    }

    private void checkAndProcessLevelUp(MemberIsland island) {
        if (island.isMaxLevel()) return;

        LevelSpec nextSpec = getLevelSpec(island.getLevel() + 1);
        if (!island.canLevelUp(nextSpec)) return;

        int previousLevel = island.getLevel();
        island.levelUp();
        eventPublisher.publishEvent(IslandLevelUpEvent.of(
                island.getMemberAccount().getId(),
                island.getId(),
                previousLevel,
                island.getLevel()
        ));
    }

    private MemberIsland getIsland(Long memberAccountId) {
        return memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
    }

    private LevelSpec getLevelSpec(int level) {
        return levelSpecCache.get(level);
    }
}
