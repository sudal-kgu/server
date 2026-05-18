package store.sonyk9919.api.domain.island.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.sonyk9919.api.domain.building.service.IslandBoostCache;
import store.sonyk9919.api.domain.island.dto.MemberIslandDto;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.domain.member.service.MemberAccountService;
import store.sonyk9919.api.domain.slot.service.SlotSetupService;

@ExtendWith(MockitoExtension.class)
class MemberIslandRegistryServiceTest {

    @Mock private MemberIslandRepository memberIslandRepository;
    @Mock private MemberAccountService memberAccountService;
    @Mock private SlotSetupService slotSetupService;
    @Mock private ResourceSetupService resourceSetupService;
    @Mock private LevelSpecCache levelSpecCache;
    @InjectMocks private MemberIslandRegistryService memberIslandRegistryService;
    @Mock private IslandBoostCache islandBoostCache;

    private static final Long MEMBER_ID = 1L;
    private MemberIsland island;
    private LevelSpec currentSpec;

    @BeforeEach
    void setUp() {
        island = mock(MemberIsland.class);
        currentSpec = mock(LevelSpec.class);
        given(memberIslandRepository.findByMemberAccountId(MEMBER_ID)).willReturn(Optional.of(island));
    }

    @Test
    void getIslandDto_최고레벨이_아닐_때_nextLevel이_포함된다() {
        // given
        given(island.isMaxLevel()).willReturn(false);
        given(island.getLevel()).willReturn(2);
        given(levelSpecCache.get(2)).willReturn(currentSpec);

        // when
        MemberIslandDto dto = memberIslandRegistryService.getIslandDto(MEMBER_ID);

        // then
        assertThat(dto.getNextLevel()).isNotNull();
    }

    @Test
    void getIslandDto_최고레벨일_때_nextLevel이_null이다() {
        // given
        given(island.isMaxLevel()).willReturn(true);

        // when
        MemberIslandDto dto = memberIslandRegistryService.getIslandDto(MEMBER_ID);

        // then
        assertThat(dto.getNextLevel()).isNull();
    }
}
