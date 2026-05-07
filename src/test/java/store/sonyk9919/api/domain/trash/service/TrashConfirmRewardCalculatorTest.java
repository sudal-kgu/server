package store.sonyk9919.api.domain.trash.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.sonyk9919.api.domain.building.service.IslandBoostCache;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

@ExtendWith(MockitoExtension.class)
class TrashConfirmRewardCalculatorTest {

    @Mock  private IslandBoostCache islandBoostCache;

    @InjectMocks private TrashConfirmRewardCalculator trashConfirmRewardCalculator;

    @Test
    @DisplayName("분리수거 보상 추가 +30 효과가 있을 때 (10+30)")
    void disposal_reward() {
        // given
        MemberIsland island = mock(MemberIsland.class);
        given(islandBoostCache.getDisposalRewardAdd(island)).willReturn(30.0);

        // when
        int finalReward = trashConfirmRewardCalculator.calculateDisposalShell(island);

        // then
        assertThat(finalReward).isEqualTo(40);
    }

    @Test
    @DisplayName("분리수거 보상 관련 건물 효과가 없으면 기본 보상")
    void default_reward() {
        // given
        MemberIsland island = mock(MemberIsland.class);
        given(islandBoostCache.getDisposalRewardAdd(island)).willReturn(0.0);

        // when
        int finalReward = trashConfirmRewardCalculator.calculateDisposalShell(island);

        // then
        assertThat(finalReward).isEqualTo(10);
    }
}