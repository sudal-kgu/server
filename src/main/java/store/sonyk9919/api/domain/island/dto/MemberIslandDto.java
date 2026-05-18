package store.sonyk9919.api.domain.island.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberIslandDto {
    private final String nickname;
    private final int level;
    private final int cumulativeExp;
    private final int recyclingContributionExp;
    private final int itemContributionExp;
    private final NextLevelConditionDto nextLevel;
    private final IslandEffectDto effects;

    public static MemberIslandDto from(MemberIsland island, NextLevelConditionDto nextLevel) {
        return new MemberIslandDto(
                island.getNickname(),
                island.getLevel(),
                island.getCumulativeExp(),
                island.getRecyclingContributionExp(),
                island.getItemContributionExp(),
                nextLevel,
                null
        );
    }

    public static MemberIslandDto from(
            MemberIsland island,
            NextLevelConditionDto nextLevel,
            IslandEffectDto effects
    ) {
        return new MemberIslandDto(
                island.getNickname(),
                island.getLevel(),
                island.getCumulativeExp(),
                island.getRecyclingContributionExp(),
                island.getItemContributionExp(),
                nextLevel,
                effects
        );
    }
}
