package store.sonyk9919.api.domain.building.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingYield;

class HarvestCalculatorTest {

    private Building building;
    private BuildingYield yield;

    @BeforeEach
    void setup() {
        building = mock(Building.class);
        yield = mock(BuildingYield.class);
        when(building.getCurrentYield()).thenReturn(yield);
    }

    @Test
    @DisplayName("1시간 가동 시 설정된 pph 만큼 보석 획득 (부스트 0%)")
    void 기본_계산() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);

        when(building.getLastCollectedAt()).thenReturn(start);
        when(building.getFuelExpiredAt()).thenReturn(start.plusHours(2));
        when(yield.getPph()).thenReturn(100);

        int gems = HarvestCalculator.of(building, start.plusHours(1)).calculate(0.0);

        assertThat(gems).isEqualTo(100);
    }

    @Test
    @DisplayName("기본 생산량에 20% 부스트 포함")
    void 부스트_포함_계산() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);

        when(building.getLastCollectedAt()).thenReturn(start);
        when(building.getFuelExpiredAt()).thenReturn(start.plusHours(2));
        when(yield.getPph()).thenReturn(100);

        int gems = HarvestCalculator.of(building, start.plusHours(1)).calculate(20.0);

        assertThat(gems).isEqualTo(120);
    }

    @Test
    @DisplayName("현재 시간이 만료 시간을 넘어도 만료 시간까지만 계산 확인")
    void 만료시간_제한() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime expired = start.plusHours(1);

        when(building.getLastCollectedAt()).thenReturn(start);
        when(building.getFuelExpiredAt()).thenReturn(expired);
        when(yield.getPph()).thenReturn(100);

        int gems = HarvestCalculator.of(building, start.plusHours(3)).calculate(0.0);

        assertThat(gems).isEqualTo(100);
    }

    @Test
    @DisplayName("가동 기록(lastCollectedAt)이 없으면 0 반환")
    void 가동기록_없음() {
        when(building.getLastCollectedAt()).thenReturn(null);
        when(building.getFuelExpiredAt()).thenReturn(LocalDateTime.now().plusHours(1));

        int gems = HarvestCalculator.of(building, LocalDateTime.now()).calculate(0.0);

        assertThat(gems).isZero();
    }
}