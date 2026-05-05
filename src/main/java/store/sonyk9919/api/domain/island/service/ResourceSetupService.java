package store.sonyk9919.api.domain.island.service;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.repository.MemberResourceRepository;

@Service
@RequiredArgsConstructor
public class ResourceSetupService {

    private final MemberResourceRepository memberResourceRepository;

    @Transactional
    public void setupResources(MemberIsland island) {
        List<MemberResource> resources = Arrays.stream(ResourceType.values())
                .map(type -> MemberResource.create(type, island))
                .toList();
        memberResourceRepository.saveAll(resources);
    }
}
