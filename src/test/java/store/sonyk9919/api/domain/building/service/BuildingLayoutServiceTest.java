package store.sonyk9919.api.domain.building.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;
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
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.slot.dto.SlotMoveRequestDto;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.domain.slot.service.SlotQueryHelper;
import store.sonyk9919.api.domain.building.service.BuildingModelUriResolver;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BuildingLayoutServiceTest {

    @InjectMocks private BuildingLayoutService buildingLayoutService;
    @InjectMocks private BuildingMoveService buildingMoveService;

    @Mock private ResourceService resourceService;
    @Mock private BuildingMetadataRepository metadataRepository;
    @Mock private BuildingRepository buildingRepository;
    @Mock private SlotRepository slotRepository;
    @Mock private SlotQueryHelper slotQueryHelper;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private BuildingModelUriResolver buildingModelUriResolver;

    private MemberIsland island;
    private Slot slot;
    private ResourceBalanceResponse mockBalance;

    @BeforeEach
    void setUp() {
        island = mock(MemberIsland.class);
        slot = mock(Slot.class);
        mockBalance = mock(ResourceBalanceResponse.class);
    }

    @Test
    @DisplayName("재화 차감 및 슬롯에 건물 배치")
    void build_Success() {
        // given
        BuildingMetadata metadata = mock(BuildingMetadata.class);
        BuildingYield yield = mock(BuildingYield.class);
        BuildingLayoutDto layoutDto = mock(BuildingLayoutDto.class);
        BuildingCategory category = mock(BuildingCategory.class);

        given(slotQueryHelper.getSlot(1L, 1)).willReturn(slot);
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
        verify(eventPublisher).publishEvent(island);
    }

    @Test
    @DisplayName("건물 철거 시 재화 환불 및 슬롯 비우기")
    void demolish_Success() {
        // given
        Building building = mock(Building.class);
        BuildingYield yield = mock(BuildingYield.class);

        given(slotQueryHelper.getSlot(1L, 1)).willReturn(slot);
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
        verify(resourceService).add(island, ResourceType.SHELL, 50);
        verify(resourceService).add(island, ResourceType.GEM, 10);
        verify(eventPublisher).publishEvent(island);
    }

    @Test
    @DisplayName("두 슬롯의 건물을 swap 한다.")
    void moveOf_Success() {
        // given
        Long memberId = 1L;
        SlotMoveRequestDto slotMoveRequestDto = SlotMoveRequestDto.of(1, 2);

        Building buildingA = stubbedBuilding("BuildingA");
        Building buildingB = stubbedBuilding("BuildingB");

        Slot fromSlot = createSlotWithBuilding(1, buildingA);
        Slot toSlot = createSlotWithBuilding(2, buildingB);

        given(slotQueryHelper.getSlot(memberId, 1)).willReturn(fromSlot);
        given(slotQueryHelper.getSlot(memberId, 2)).willReturn(toSlot);

        // when
        buildingMoveService.moveOf(memberId, slotMoveRequestDto);

        // then
        assertThat(fromSlot.getBuilding()).isEqualTo(buildingB);
        assertThat(toSlot.getBuilding()).isEqualTo(buildingA);
        verify(slotRepository).flush();
    }

    private Building stubbedBuilding(String name) {
        BuildingMetadata metadata = mock(BuildingMetadata.class);
        BuildingYield yield = mock(BuildingYield.class);
        Building building = mock(Building.class);

        given(building.getBuildingMetadata()).willReturn(metadata);
        given(building.getCurrentYield()).willReturn(yield);
        given(metadata.getName()).willReturn(name);
        given(metadata.getCategory()).willReturn(mock(BuildingCategory.class));
        given(yield.getLevel()).willReturn(1);

        return building;
    }

    private Slot createSlotWithBuilding(int slotNumber, Building building) {
        Slot slot = Slot.of(island, slotNumber);
        slot.activate();
        slot.build(building);
        return slot;
    }
}