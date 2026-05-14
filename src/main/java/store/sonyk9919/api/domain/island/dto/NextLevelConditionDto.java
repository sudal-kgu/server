package store.sonyk9919.api.domain.island.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.entity.LevelSpec;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NextLevelConditionDto {
    private final int totalRequiredExp;
    private final int recyclingExpLimit;

    public static NextLevelConditionDto from(LevelSpec currentSpec) {
        return new NextLevelConditionDto(
                currentSpec.getRequiredExp(),
                currentSpec.getRecyclingContributionExpLimit()
        );
    }
}
