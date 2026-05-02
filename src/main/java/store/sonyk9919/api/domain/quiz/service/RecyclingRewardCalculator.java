package store.sonyk9919.api.domain.quiz.service;

import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

@Component
public class RecyclingRewardCalculator {

    private static final int BASE_SHELL = 10;

    public int calculateShell(MemberIsland island) {
        return BASE_SHELL;
    }
}
