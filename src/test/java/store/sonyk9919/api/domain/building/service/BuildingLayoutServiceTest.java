package store.sonyk9919.api.domain.building.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.sonyk9919.api.domain.building.dto.BuildingLayoutDto;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingCategory;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.entity.BuildingYield;
import store.sonyk9919.api.domain.building.repository.BuildingMetadataRepository;
import store.sonyk9919.api.domain.building.repository.BuildingRepository;
import store.sonyk9919.api.domain.island.dto.ResourceBalanceResponse;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;

@ExtendWith(MockitoExtension.class)
class BuildingLayoutServiceTest {

    @InjectMocks private BuildingLayoutService buildingLayoutService;

    @Mock private MemberIslandRegistryService memberIslandService;
    @Mock private SlotRepository slotRepository;
    @Mock private ResourceService resourceService;
    @Mock private IslandBoostCache islandBoostCache;
    @Mock private BuildingMetadataRepository metadataRepository;
    @Mock private BuildingRepository buildingRepository;

    private MemberIsland island;
    private Slot slot;
    private ResourceBalanceResponse mockBalance;

    @BeforeEach
    void setUp() {
        island = mock(MemberIsland.class);
        slot = mock(Slot.class);
        mockBalance = mock(ResourceBalanceResponse.class);

        given(memberIslandService.getIsland(1L)).willReturn(island);
        given(slotRepository.findByIslandAndSlotNumber(island, 1)).willReturn(Optional.of(slot));
    }

    @Test
    @DisplayName("재화 차감 및 슬롯에 건물 배치")
    void build_Success() {
        // given
        BuildingMetadata metadata = mock(BuildingMetadata.class);
        BuildingYield yield = mock(BuildingYield.class);
        BuildingLayoutDto layoutDto = mock(BuildingLayoutDto.class);
        BuildingCategory category = mock(BuildingCategory.class);

        given(slot.getIsland()).willReturn(island);
        given(island.getLevel()).willReturn(2);
        given(layoutDto.getBuildingMetadataId()).willReturn(10L);

        given(metadataRepository.findById(10L)).willReturn(Optional.of(metadata));
        given(metadata.getCategory()).willReturn(category);
        given(category.name()).willReturn("PRODUCTION");

        given(metadata.getYieldForLevel(1)).willReturn(yield);
        given(yield.getRequiredLevel()).willReturn(1);
        given(yield.getCostShells()).willReturn(100);
        given(yield.getCostGems()).willReturn(20);

        given(resourceService.getBalance(1L)).willReturn(mockBalance);

        // when
        buildingLayoutService.buildOf(1L, 1, layoutDto);

        // then
        verify(resourceService).subtract(island, ResourceType.SHELL, 100);
        verify(resourceService).subtract(island, ResourceType.GEM, 20);
        verify(slot).build(any(Building.class));
        verify(buildingRepository).save(any(Building.class));
        verify(islandBoostCache).evictBoostCache(island);
    }

    @Test
    @DisplayName("건물 철거 시 재화 환불 및 슬롯 비우기")
    void demolish_Success() {
        // given
        Building building = mock(Building.class);
        BuildingYield yield = mock(BuildingYield.class);

        given(slot.getIsland()).willReturn(island);
        given(slot.hasBuilding()).willReturn(true);
        given(slot.getBuilding()).willReturn(building);
        given(building.getCurrentYield()).willReturn(yield);
        given(yield.getRefundShell()).willReturn(50);
        given(yield.getRefundGem()).willReturn(10);
        given(resourceService.getBalance(1L)).willReturn(mockBalance);

        // when
        buildingLayoutService.demolishOf(1L, 1);

        // then
        verify(slot).demolish();
        verify(islandBoostCache).evictBoostCache(island);
        verify(resourceService).add(island, ResourceType.SHELL, 50);
        verify(resourceService).add(island, ResourceType.GEM, 10);
    }
}