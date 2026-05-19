package store.sonyk9919.api.domain.island.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.global.dto.PageResponseDto;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IslandRankingResponse {

    private final PageResponseDto<IslandRankingEntryDto> rankings;
    private final IslandRankingEntryDto me;

    public static IslandRankingResponse of(PageResponseDto<IslandRankingEntryDto> rankings, IslandRankingEntryDto me) {
        return new IslandRankingResponse(rankings, me);
    }
}
