package store.sonyk9919.api.domain.quiz.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.dto.LevelUpResult;
import store.sonyk9919.api.domain.island.dto.MemberIslandDto;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RecyclingRewardResponse {

    private final int earnedShell;
    private final int earnedFuel;
    private final int earnedExp;
    private final MemberIslandDto island;
    private final LevelUpResult.UnlockNotice notice;

    public static RecyclingRewardResponse of(int shell, int fuel, int exp, LevelUpResult levelUpResult) {
        return new RecyclingRewardResponse(shell, fuel, exp, levelUpResult.getIsland(), levelUpResult.getNotice());
    }
}
