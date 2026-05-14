package store.sonyk9919.api.domain.island.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import store.sonyk9919.api.domain.island.entity.LevelSpec;

@ExtendWith(MockitoExtension.class)
class NextLevelConditionDtoTest {

    @Test
    void from_totalRequiredExp와_recyclingExpLimit이_currentSpec_값으로_설정된다() {
        // given
        LevelSpec spec = mockSpec(3000, 1000);

        // when
        NextLevelConditionDto dto = NextLevelConditionDto.from(spec);

        // then
        assertThat(dto.getTotalRequiredExp()).isEqualTo(3000);
        assertThat(dto.getRecyclingExpLimit()).isEqualTo(1000);
    }

    private LevelSpec mockSpec(int requiredExp, int recyclingExpLimit) {
        LevelSpec spec = mock(LevelSpec.class);
        given(spec.getRequiredExp()).willReturn(requiredExp);
        given(spec.getRecyclingContributionExpLimit()).willReturn(recyclingExpLimit);
        return spec;
    }
}
