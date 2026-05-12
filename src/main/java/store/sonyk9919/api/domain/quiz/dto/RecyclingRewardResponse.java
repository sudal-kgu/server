package store.sonyk9919.api.domain.quiz.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.dto.LevelUpResult;
import store.sonyk9919.api.domain.island.dto.MemberIslandDto;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RecyclingRewardResponse {

    private final Earned earned;
    private final MemberIslandDto island;
    private final LevelUpResult.UnlockNotice notice;

    public static RecyclingRewardResponse of(int shell, int fuel, int exp, LevelUpResult levelUpResult) {
        return new RecyclingRewardResponse(
                new Earned(shell, fuel, exp),
                levelUpResult.getIsland(),
                levelUpResult.getNotice()
        );
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Earned {
        private final int shell;
        private final int fuel;
        private final int exp;
    }
}
