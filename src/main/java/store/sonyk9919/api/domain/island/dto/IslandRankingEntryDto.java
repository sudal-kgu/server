package store.sonyk9919.api.domain.island.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.Region;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IslandRankingEntryDto {

    private final int rank;
    private final String nickname;
    private final Region region;
    private final int level;
    private final int cumulativeExp;

    public static IslandRankingEntryDto of(int rank, MemberIsland island) {
        return new IslandRankingEntryDto(
                rank,
                island.getNickname(),
                island.getRegion(),
                island.getLevel(),
                island.getCumulativeExp()
        );
    }
}
