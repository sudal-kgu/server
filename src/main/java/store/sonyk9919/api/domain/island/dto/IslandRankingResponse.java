package store.sonyk9919.api.domain.island.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IslandRankingResponse {

    private final List<IslandRankingEntryDto> rankings;
    private final IslandRankingEntryDto me;

    public static IslandRankingResponse of(List<IslandRankingEntryDto> rankings, IslandRankingEntryDto me) {
        return new IslandRankingResponse(rankings, me);
    }
}
