package store.sonyk9919.api.domain.island.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NextLevelConditionDto {
    private final int totalRequiredExp;
    private final int recyclingExpLimit;
    private final boolean itemRequired;

    public static NextLevelConditionDto from(LevelSpec currentSpec, MemberIsland island) {
        int remainingExp = currentSpec.getRequiredExp() - island.getCumulativeExp();
        int remainingRecyclingExp = currentSpec.getRecyclingContributionExpLimit() - island.getRecyclingContributionExp();
        return new NextLevelConditionDto(
                currentSpec.getRequiredExp(),
                currentSpec.getRecyclingContributionExpLimit(),
                remainingRecyclingExp < remainingExp
        );
    }
}
