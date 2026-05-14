package store.sonyk9919.api.domain.island.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.island.dto.IslandEffectResponseDto;
import store.sonyk9919.api.domain.island.dto.ItemCatalogDto;
import store.sonyk9919.api.domain.island.dto.MemberIslandCreateRequestDto;
import store.sonyk9919.api.domain.island.dto.MemberIslandDto;
import store.sonyk9919.api.domain.island.service.IslandEffectQueryService;
import store.sonyk9919.api.domain.island.service.ItemUsageService;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;

import java.util.List;

@RestController
@RequestMapping("/v1/islands")
@RequiredArgsConstructor
public class MemberIslandController {

    private final MemberIslandRegistryService memberIslandRegistryService;
    private final IslandEffectQueryService effectQueryService;
    private final ItemUsageService itemUsageService;

    @Operation(summary = "섬 생성", description = "인증된 회원의 새로운 섬을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "섬 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (이미 섬이 존재하거나 닉네임 형식 오류)"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberIslandDto createIsland(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember,
            @Validated @RequestBody MemberIslandCreateRequestDto requestDto
    ) {
        return memberIslandRegistryService.createDto(authMember.getId(), requestDto.getNickname(), requestDto.getRegion());
    }

    @Operation(summary = "섬 정보 조회", description = "회원의 섬 정보를 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "섬을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MemberIslandDto getIsland(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember
    ) {
        return memberIslandRegistryService.getIslandDto(authMember.getId());
    }

    @Operation(summary = "아이템 사용량 조회", description = "회원의 섬에서 아이템별 현재 사용량을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "섬을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/items/usages")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemCatalogDto> getItemUsages(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember
    ) {
        return itemUsageService.getItemUsages(authMember.getId());
    }

    @Operation(
            summary = "섬 건물 효과 조회",
            description = "섬에 적용 중인 모든 건물 효과를 조회합니다."
    )
    @GetMapping("/effects")
    public IslandEffectResponseDto getIslandEffects(
            @AuthMember AuthMemberDto authMember
    ) {
        return effectQueryService.getEffects(authMember.getId());
    }
}
