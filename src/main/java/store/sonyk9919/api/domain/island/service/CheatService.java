package store.sonyk9919.api.domain.island.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.dto.ResourceBalanceResponse;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.domain.island.repository.MemberResourceRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional
public class CheatService {

    private static final long CHEAT_RESOURCE_AMOUNT = 10_000_000L;

    private final MemberIslandRepository memberIslandRepository;
    private final MemberResourceRepository memberResourceRepository;

    public ResourceBalanceResponse apply(Long memberAccountId) {
        MemberIsland island = memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
        island.forceMaxLevel();

        List<MemberResource> resources = memberResourceRepository.findAllByIslandMemberAccountId(memberAccountId);
        resources.forEach(r -> r.overrideAmount(CHEAT_RESOURCE_AMOUNT));

        return ResourceBalanceResponse.from(resources);
    }
}
