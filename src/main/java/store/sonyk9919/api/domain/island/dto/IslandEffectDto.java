package store.sonyk9919.api.domain.island.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IslandEffectDto {

    private double islandBoost;
    private double quizRewardBoost;
    private double quizRewardAdd;

    public static IslandEffectDto of(
            double islandBoost,
            double quizRewardBoost,
            double quizRewardAdd
    ){
      return new IslandEffectDto(islandBoost, quizRewardBoost, quizRewardAdd);
    }
}
