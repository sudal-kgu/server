package store.sonyk9919.api.domain.island.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.repository.MemberResourceRepository;

@Service
@RequiredArgsConstructor
public class ResourceSlotService {

    private final MemberResourceRepository memberResourceRepository;

    @Transactional
    public MemberResource subtractShell(MemberIsland island, long amount) {
        MemberResource shellResource = memberResourceRepository
                .findByIslandAndResourceType(island, ResourceType.SHELL);

        shellResource.subtractAmount(amount);
        return shellResource;
    }
}