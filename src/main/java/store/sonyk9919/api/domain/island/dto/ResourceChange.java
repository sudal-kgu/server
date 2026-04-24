package store.sonyk9919.api.domain.island.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.entity.OperationType;
import store.sonyk9919.api.domain.island.entity.ResourceType;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceChange {

    private final ResourceType type;
    private final long delta;
    private final OperationType operation;

    public static ResourceChange add(ResourceType type, long amount) {
        return new ResourceChange(type, amount, OperationType.ADD);
    }

    public static ResourceChange subtract(ResourceType type, long amount) {
        return new ResourceChange(type, amount, OperationType.SUBTRACT);
    }
}
