package store.sonyk9919.api.domain.slot.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SlotUnlockPolicy {
    private static final int BASE_COST = 1_000;
    private static final double COST_PER_ACTIVE_SLOT = 1.5;

    public static int costFor(int activeCount) {
        return (int) (BASE_COST * Math.pow(COST_PER_ACTIVE_SLOT, activeCount));
    }
}