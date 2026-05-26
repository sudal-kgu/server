package store.sonyk9919.api.domain.quiz.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.dto.LevelUpResult;
import store.sonyk9919.api.domain.island.dto.MemberIslandDto;
import store.sonyk9919.api.domain.island.dto.ResourceBalanceResponse;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RecyclingRewardResponse {

    private final EarnedResource earned;
    private final ResourceBalanceResponse resource;
    private final MemberIslandDto island;
    private final LevelUpResult.UnlockNotice notice;

    public static RecyclingRewardResponse of(int shell, int fuel, int exp, ResourceBalanceResponse resource, LevelUpResult levelUpResult) {
        return new RecyclingRewardResponse(EarnedResource.of(shell, fuel, exp), resource, levelUpResult.getIsland(), levelUpResult.getNotice());
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static class EarnedResource {
        private final int shell;
        private final int fuel;
        private final int exp;

        public static EarnedResource of(int shell, int fuel, int exp) {
            return new EarnedResource(shell, fuel, exp);
        }
    }
}
