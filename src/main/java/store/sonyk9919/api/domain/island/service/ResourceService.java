package store.sonyk9919.api.domain.island.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.dto.ResourceBalanceResponse;
import store.sonyk9919.api.domain.island.dto.ResourceChange;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.exception.ResourceStatus;
import store.sonyk9919.api.domain.island.repository.MemberResourceRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceService {

    private final MemberResourceRepository memberResourceRepository;

    @Transactional
    public MemberResource add(MemberIsland island, ResourceType type, long amount) {
        MemberResource resource = memberResourceRepository
                .findByIslandAndResourceType(island, type)
                .orElseThrow(() -> new CustomException(ResourceStatus.RESOURCE_NOT_FOUND));
        resource.addAmount(amount);
        return resource;
    }

    @Transactional
    public MemberResource subtract(MemberIsland island, ResourceType type, long amount) {
        MemberResource resource = memberResourceRepository
                .findByIslandAndResourceType(island, type)
                .orElseThrow(() -> new CustomException(ResourceStatus.RESOURCE_NOT_FOUND));
        resource.subtractAmount(amount);
        return resource;
    }
    
    @Transactional
    public List<MemberResource> applyMultiple(MemberIsland island, List<ResourceChange> changes) {
        return changes.stream()
                .sorted(Comparator.comparingInt(c -> c.getType().ordinal()))
                .map(change -> {
                    MemberResource resource = memberResourceRepository
                            .findByIslandAndResourceType(island, change.getType())
                            .orElseThrow(() -> new CustomException(ResourceStatus.RESOURCE_NOT_FOUND));
                    switch (change.getOperation()) {
                        case ADD -> resource.addAmount(change.getDelta());
                        case SUBTRACT -> resource.subtractAmount(change.getDelta());
                    }
                    return resource;
                })
                .toList();
    }

    public ResourceBalanceResponse getBalance(Long memberAccountId) {
        List<MemberResource> resources = memberResourceRepository.findAllByIslandMemberAccountId(memberAccountId);
        if (resources.size() != ResourceType.values().length)
            throw new CustomException(ResourceStatus.RESOURCE_NOT_FOUND);
        return ResourceBalanceResponse.from(resources);
    }
}
