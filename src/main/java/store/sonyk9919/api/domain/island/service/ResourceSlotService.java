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
public class ResourceSlotService {

    private final MemberResourceRepository memberResourceRepository;

    @Transactional
    public MemberResource subtractShell(MemberIsland island, long amount) {
        MemberResource shellResource = memberResourceRepository
                .findWithLockByIslandAndResourceType(island, ResourceType.SHELL)
                .orElseThrow(() -> new CustomException(ResourceStatus.RESOURCE_NOT_FOUND));

        shellResource.subtractAmount(amount);
        return shellResource;
    }
}