package store.sonyk9919.api.domain.island.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceBalanceResponse {

    private final long shell;
    private final long gem;
    private final long fuel;

    public static ResourceBalanceResponse from(List<MemberResource> resources) {
        Map<ResourceType, Long> amounts = resources.stream()
                .collect(Collectors.toMap(MemberResource::getResourceType, MemberResource::getAmount));

        return new ResourceBalanceResponse(
                amounts.getOrDefault(ResourceType.SHELL, 0L),
                amounts.getOrDefault(ResourceType.GEM, 0L),
                amounts.getOrDefault(ResourceType.FUEL, 0L)
        );
    }
}
