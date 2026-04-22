package store.sonyk9919.api.domain.building.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.building.service.BuildingHarvestService;
import store.sonyk9919.api.domain.building.service.BuildingOperateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/buildings")
public class BuildingHarvestController {

    private final BuildingHarvestService harvestService;
    private final BuildingOperateService operateService;


    @PostMapping("/{slotNumber}/operate")
    public void injectFuel(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        operateService.operate(authMember.getId(), slotNumber);
    }

    @PostMapping("/{slotNumber}/harvest")
    public int harvestBuilding(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        return harvestService.harvest(authMember.getId(), slotNumber);
    }

    @PostMapping("/harvests")
    public int harvestBuildings(
            @AuthMember AuthMemberDto authMember
    ) {
        return harvestService.harvestAll(authMember.getId());
    }
}