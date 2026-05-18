package store.sonyk9919.api.domain.island.service;

import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.dto.IslandRankingEntryDto;
import store.sonyk9919.api.domain.island.dto.IslandRankingResponse;

import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.Region;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IslandRankingService {

    private static final int RANKING_SIZE = 100;
    private static final Pageable RANKING_PAGEABLE = PageRequest.of(0, RANKING_SIZE);

    private final MemberIslandRepository memberIslandRepository;

    public IslandRankingResponse getRanking(Region region, Long memberAccountId) {
        if (region == null) {
            return getOverallRanking(memberAccountId);
        }
        return getRegionalRanking(region, memberAccountId);
    }

    private IslandRankingResponse getOverallRanking(Long memberAccountId) {
        List<MemberIsland> top = memberIslandRepository.findAllByOrderByCumulativeExpDesc(RANKING_PAGEABLE).getContent();
        MemberIsland island = findMyIsland(memberAccountId);
        int ranking = memberIslandRepository.countByCumulativeExpGreaterThan(island.getCumulativeExp()) + 1;
        return IslandRankingResponse.of(toRankingEntries(top), IslandRankingEntryDto.of(ranking, island));
    }

    private MemberIsland findMyIsland(Long memberAccountId) {
        return memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
    }

    private List<IslandRankingEntryDto> toRankingEntries(List<MemberIsland> islands) {
        return IntStream.range(0, islands.size())
                .mapToObj(i -> IslandRankingEntryDto.of(i + 1, islands.get(i)))
                .toList();
    }

    private IslandRankingResponse getRegionalRanking(Region region, Long memberAccountId) {
        List<MemberIsland> top = memberIslandRepository.findAllByRegionOrderByCumulativeExpDesc(region, RANKING_PAGEABLE).getContent();
        MemberIsland island = findMyIsland(memberAccountId);

        IslandRankingEntryDto me = null;
        if (island.isSameRegion(region)) {
            int ranking = memberIslandRepository.countByRegionAndCumulativeExpGreaterThan(region, island.getCumulativeExp()) + 1;
            me = IslandRankingEntryDto.of(ranking, island);
        }
        return IslandRankingResponse.of(toRankingEntries(top), me);
    }
}
