package store.sonyk9919.api.domain.island.service;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import store.sonyk9919.api.global.dto.PageResponseDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IslandRankingService {

    private final MemberIslandRepository memberIslandRepository;

    public IslandRankingResponse getRanking(Region region, Long memberAccountId, Pageable pageable) {
        if (region == null) {
            return getOverallRanking(memberAccountId, pageable);
        }
        return getRegionalRanking(region, memberAccountId, pageable);
    }

    private IslandRankingResponse getOverallRanking(Long memberAccountId, Pageable pageable) {
        Page<MemberIsland> page = memberIslandRepository.findAllByOrderByCumulativeExpDesc(pageable);
        MemberIsland island = findMyIsland(memberAccountId);
        int ranking = memberIslandRepository.countByCumulativeExpGreaterThan(island.getCumulativeExp()) + 1;
        return IslandRankingResponse.of(toRankingPage(page), IslandRankingEntryDto.of(ranking, island));
    }

    private MemberIsland findMyIsland(Long memberAccountId) {
        return memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
    }

    private PageResponseDto<IslandRankingEntryDto> toRankingPage(Page<MemberIsland> page) {
        int offset = page.getNumber() * page.getSize();
        AtomicInteger rank = new AtomicInteger(offset + 1);
        return PageResponseDto.from(page, island -> IslandRankingEntryDto.of(rank.getAndIncrement(), island));
    }

    private IslandRankingResponse getRegionalRanking(Region region, Long memberAccountId, Pageable pageable) {
        Page<MemberIsland> page = memberIslandRepository.findAllByRegionOrderByCumulativeExpDesc(region, pageable);
        MemberIsland island = findMyIsland(memberAccountId);

        IslandRankingEntryDto me = null;
        if (island.isSameRegion(region)) {
            int ranking = memberIslandRepository.countByRegionAndCumulativeExpGreaterThan(region, island.getCumulativeExp()) + 1;
            me = IslandRankingEntryDto.of(ranking, island);
        }
        return IslandRankingResponse.of(toRankingPage(page), me);
    }
}
