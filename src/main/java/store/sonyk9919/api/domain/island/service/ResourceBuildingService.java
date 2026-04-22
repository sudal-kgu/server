package store.sonyk9919.api.domain.island.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.exception.ResourceStatus;
import store.sonyk9919.api.domain.island.repository.MemberResourceRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
public class ResourceBuildingService {

    private final MemberResourceRepository memberResourceRepository;

    @Transactional
    public void subtractResource(MemberIsland island, int costShells, int costGems) {
        if (costShells > 0)  subtractByType(island, ResourceType.SHELL, costShells);
        if (costGems > 0) subtractByType(island, ResourceType.GEM, costGems);
    }

    @Transactional
    public void subtractFuel(MemberIsland island, int fuel) {
        if (fuel > 0) subtractByType(island, ResourceType.FUEL, fuel);
    }

    @Transactional
    public void addResource(MemberIsland island, int refundShells, int refundGems) {
        if (refundShells > 0) addByType(island, ResourceType.SHELL, refundShells);
        if (refundGems > 0) addByType(island, ResourceType.GEM, refundGems);
    }

    @Transactional
    public void addGems(MemberIsland island, int gem) {
        if (gem > 0) addByType(island, ResourceType.GEM, gem);
    }
    
    private void subtractByType(MemberIsland island, ResourceType type, int cost){
        MemberResource resource = memberResourceRepository
                .findByIslandAndResourceType(island, type)
                .orElseThrow(() -> new CustomException(ResourceStatus.RESOURCE_NOT_FOUND));

        resource.subtractAmount(cost);
    }

    private void addByType(MemberIsland island, ResourceType type, int amount) {
        MemberResource resource = memberResourceRepository
                .findByIslandAndResourceType(island, type)
                .orElseThrow(() -> new CustomException(ResourceStatus.RESOURCE_NOT_FOUND));

        resource.addAmount(amount);
    }
}