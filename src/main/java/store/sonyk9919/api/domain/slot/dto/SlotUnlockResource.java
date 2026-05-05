package store.sonyk9919.api.domain.slot.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.island.entity.ResourceType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SlotUnlockResource {
    private ResourceType resourceType;
    private int unlockCost;

    public static SlotUnlockResource of(ResourceType type, int unlockCost) {
        return new SlotUnlockResource(type, unlockCost);
    }
}