package store.sonyk9919.api.domain.slot.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SlotUnlockResource {

    private int shell;

    public static SlotUnlockResource of(int unlockCost) {
        return new SlotUnlockResource(unlockCost);
    }
}