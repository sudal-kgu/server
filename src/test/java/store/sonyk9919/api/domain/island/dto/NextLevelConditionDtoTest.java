package store.sonyk9919.api.domain.island.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

@ExtendWith(MockitoExtension.class)
class NextLevelConditionDtoTest {

    @Test
    void from_레벨1_초기상태_itemRequired가_false다() {
        // given
        LevelSpec spec = mockSpec(500, 500);
        MemberIsland island = mockIsland(0, 0);

        // when
        NextLevelConditionDto dto = NextLevelConditionDto.from(spec, island);

        // then
        assertThat(dto.isItemRequired()).isFalse();
    }

    private LevelSpec mockSpec(int requiredExp, int recyclingExpLimit) {
        LevelSpec spec = mock(LevelSpec.class);
        given(spec.getRequiredExp()).willReturn(requiredExp);
        given(spec.getRecyclingContributionExpLimit()).willReturn(recyclingExpLimit);
        return spec;
    }

    private MemberIsland mockIsland(int cumulativeExp, int recyclingContributionExp) {
        MemberIsland island = mock(MemberIsland.class);
        given(island.getCumulativeExp()).willReturn(cumulativeExp);
        given(island.getRecyclingContributionExp()).willReturn(recyclingContributionExp);
        return island;
    }

    @Test
    void from_레벨2_진입직후_itemRequired가_false다() {
        // given
        LevelSpec spec = mockSpec(1500, 1000);
        MemberIsland island = mockIsland(500, 0);

        // when
        NextLevelConditionDto dto = NextLevelConditionDto.from(spec, island);

        // then
        assertThat(dto.isItemRequired()).isFalse();
    }

    @Test
    void from_레벨3_진입직후_itemRequired가_true다() {
        // given
        LevelSpec spec = mockSpec(3000, 1000);
        MemberIsland island = mockIsland(1500, 0);

        // when
        NextLevelConditionDto dto = NextLevelConditionDto.from(spec, island);

        // then
        assertThat(dto.isItemRequired()).isTrue();
    }

    @Test
    void from_분리배출_한도_소진_시_itemRequired가_true다() {
        // given
        LevelSpec spec = mockSpec(3000, 1000);
        MemberIsland island = mockIsland(2300, 1000);

        // when
        NextLevelConditionDto dto = NextLevelConditionDto.from(spec, island);

        // then
        assertThat(dto.isItemRequired()).isTrue();
    }

    @Test
    void from_아이템_선구매로_남은exp가_분리배출가능량보다_적으면_itemRequired가_false다() {
        // given
        LevelSpec spec = mockSpec(3000, 1000);
        MemberIsland island = mockIsland(2800, 0);

        // when
        NextLevelConditionDto dto = NextLevelConditionDto.from(spec, island);

        // then
        assertThat(dto.isItemRequired()).isFalse();
    }

    @Test
    void from_totalRequiredExp와_recyclingExpLimit이_currentSpec_값으로_설정된다() {
        // given
        LevelSpec spec = mockSpec(3000, 1000);
        MemberIsland island = mockIsland(1500, 0);

        // when
        NextLevelConditionDto dto = NextLevelConditionDto.from(spec, island);

        // then
        assertThat(dto.getTotalRequiredExp()).isEqualTo(3000);
        assertThat(dto.getRecyclingExpLimit()).isEqualTo(1000);
    }
}
