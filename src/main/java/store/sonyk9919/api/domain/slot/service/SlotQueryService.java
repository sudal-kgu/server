package store.sonyk9919.api.domain.slot.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingDetailDto;
import store.sonyk9919.api.domain.building.dto.BuildingInfoDto;
import store.sonyk9919.api.domain.building.dto.ProductionInfoDto;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.entity.BuildingYield;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.slot.dto.SlotDetailResponseDto;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlotQueryService {

    private final SlotRepository slotRepository;
    private final MemberIslandRegistryService memberIslandService;

    public List<SlotResponseDto> getAllSlot(Long memberId) {
        MemberIsland island = memberIslandService.getIsland(memberId);

        return slotRepository.findAllByIsland(island)
                .stream()
                .map(slot -> SlotResponseDto.of(slot, mapToBuildingInfo(slot.getBuilding())))
                .collect(Collectors.toList());
    }

    public SlotDetailResponseDto getSlotDetail(Long memberId, Integer slotNumber) {
        MemberIsland island = memberIslandService.getIsland(memberId);

        return slotRepository.findByIslandAndSlotNumber(island, slotNumber)
                .map(slot -> SlotDetailResponseDto.of(slot, mapToBuildingDetail(slot.getBuilding())))
                .orElseThrow(() -> new CustomException(SlotStatus.SLOT_NOT_FOUND));
    }

    private BuildingInfoDto mapToBuildingInfo(Building building){
        if (building == null) return null;

        return BuildingInfoDto.of(building, building.getBuildingMetadata());
    }

    private BuildingDetailDto mapToBuildingDetail(Building building) {
        if (building == null) return null;

        BuildingMetadata metadata = building.getBuildingMetadata();
        BuildingYield yield = metadata.getYieldForLevel(building.getCurrentLevel());

        ProductionInfoDto productionInfoDto = metadata.isProductionType()
                ? ProductionInfoDto.of(building, yield)
                : null;

        return BuildingDetailDto.of(building, metadata, productionInfoDto);
    }
}