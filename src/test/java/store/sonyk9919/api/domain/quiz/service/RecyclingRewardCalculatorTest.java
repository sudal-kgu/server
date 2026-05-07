package store.sonyk9919.api.domain.quiz.service;

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
class RecyclingRewardCalculatorTest {

    @Mock private IslandBoostCache islandBoostCache;

    @InjectMocks private RecyclingRewardCalculator recyclingRewardCalculator;

    @Test
    @DisplayName("퀴즈 보상: 섬에 정화 시설(20%) 효과가 있을 때 (15 * 1.2 == 18)")
    void effect_reward() {
        // given
        MemberIsland island = mock(MemberIsland.class);

        given(islandBoostCache.getQuizRewardAdd(island)).willReturn(5.0);
        given(islandBoostCache.getQuizRewardBoost(island)).willReturn(0.2);

        // when
        int finalReward = recyclingRewardCalculator.calculateShell(island);

        // then
        assertThat(finalReward).isEqualTo(18);
    }

    @Test
    @DisplayName("퀴즈 관련 건물 효과가 없으면 기본 보상")
    void default_reward() {
        // given
        MemberIsland island = mock(MemberIsland.class);

        given(islandBoostCache.getQuizRewardAdd(island)).willReturn(0.0);
        given(islandBoostCache.getQuizRewardBoost(island)).willReturn(0.0);

        // when
        int finalReward = recyclingRewardCalculator.calculateShell(island);

        // then
        assertThat(finalReward).isEqualTo(10);
    }
}