package store.sonyk9919.api.domain.island.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.island.dto.IslandRankingEntryDto;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.Region;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.domain.member.entity.AccountRole;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.member.repository.MemberAccountRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.config.QueryDslConfig;

@Transactional
@DataJpaTest
@Import({QueryDslConfig.class, IslandRankingService.class})
class IslandRankingServiceTest {

    @Autowired private IslandRankingService islandRankingService;
    @Autowired private MemberAccountRepository memberAccountRepository;
    @Autowired private MemberIslandRepository memberIslandRepository;

    private MemberAccount myAccount;

    @BeforeEach
    void setUp() {
        myAccount = saveAccount("my-account");
        saveIsland("내 섬", Region.SEOUL, myAccount, 500);

        saveIsland("서울A", Region.SEOUL, saveAccount("seoul-a"), 1000);
        saveIsland("서울B", Region.SEOUL, saveAccount("seoul-b"), 800);
        saveIsland("서울C", Region.SEOUL, saveAccount("seoul-c"), 300);

        saveIsland("경기A", Region.GYEONGGI, saveAccount("gyeonggi-a"), 1500);
        saveIsland("경기B", Region.GYEONGGI, saveAccount("gyeonggi-b"), 200);
    }


    private MemberAccount saveAccount(String providerId) {
        OAuthUserInfoDto userInfo = mock(OAuthUserInfoDto.class);
        given(userInfo.getId()).willReturn(providerId);
        return memberAccountRepository.save(
                MemberAccount.from(userInfo, OAuthProviderType.KAKAO, AccountRole.USER)
        );
    }

    private MemberIsland saveIsland(String nickname, Region region, MemberAccount account, int exp) {
        MemberIsland island = MemberIsland.create(nickname, region, account);
        island.addItemExp(exp);
        return memberIslandRepository.save(island);
    }

    @Test
    void 전체_랭킹은_exp_내림차순으로_반환된다() {
        // when
        List<IslandRankingEntryDto> rankings = islandRankingService
                .getRanking(null, myAccount.getId(), PageRequest.of(0, 10))
                .getRankings().getContent();

        // then
        for (int i = 0; i < rankings.size() - 1; i++) {
            assertThat(rankings.get(i).getCumulativeExp())
                    .isGreaterThanOrEqualTo(rankings.get(i + 1).getCumulativeExp());
        }
    }

    @Test
    void 다음_페이지의_rank는_이전_페이지에_이어서_시작한다() {
        // when
        int lastRankOnPage1 = islandRankingService
                .getRanking(null, myAccount.getId(), PageRequest.of(0, 3))
                .getRankings().getContent()
                .stream().mapToInt(IslandRankingEntryDto::getRank).max().orElseThrow();
        int firstRankOnPage2 = islandRankingService
                .getRanking(null, myAccount.getId(), PageRequest.of(1, 3))
                .getRankings().getContent()
                .get(0).getRank();

        // then
        assertThat(firstRankOnPage2).isEqualTo(lastRankOnPage1 + 1);
    }

    @Test
    void 전체_랭킹_조회_시_내_순위가_포함된다() {
        // given
        int expectedRank = memberIslandRepository.countByCumulativeExpGreaterThan(500) + 1;

        // when
        IslandRankingEntryDto me = islandRankingService
                .getRanking(null, myAccount.getId(), PageRequest.of(0, 10))
                .getMe();

        // then
        assertThat(me).isNotNull();
        assertThat(me.getRank()).isEqualTo(expectedRank);
    }

    @Test
    void 지역_랭킹은_해당_지역_섬만_포함된다() {
        // when
        List<IslandRankingEntryDto> rankings = islandRankingService
                .getRanking(Region.SEOUL, myAccount.getId(), PageRequest.of(0, 10))
                .getRankings().getContent();

        // then
        assertThat(rankings).allMatch(r -> r.getRegion() == Region.SEOUL);
    }

    @Test
    void 지역_랭킹_조회_시_같은_지역이면_내_순위가_포함된다() {
        // given
        int expectedRank = memberIslandRepository.countByRegionAndCumulativeExpGreaterThan(Region.SEOUL, 500) + 1;

        // when
        IslandRankingEntryDto me = islandRankingService
                .getRanking(Region.SEOUL, myAccount.getId(), PageRequest.of(0, 10))
                .getMe();

        // then
        assertThat(me).isNotNull();
        assertThat(me.getRank()).isEqualTo(expectedRank);
    }

    @Test
    void 지역_랭킹_조회_시_다른_지역이면_내_순위가_null이다() {
        // when & then
        assertThat(islandRankingService
                .getRanking(Region.GYEONGGI, myAccount.getId(), PageRequest.of(0, 10))
                .getMe()).isNull();
    }

    @Test
    void 섬이_없는_사용자가_랭킹_조회_시_예외가_발생한다() {
        // given
        MemberAccount noIslandAccount = saveAccount("no-island");

        // when & then
        assertThatThrownBy(() -> islandRankingService.getRanking(null, noIslandAccount.getId(), PageRequest.of(0, 10)))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(IslandStatus.NOT_FOUND_ISLAND.getMessage());
    }
}
