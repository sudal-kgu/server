package store.sonyk9919.api.domain.island.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.island.dto.IslandRankingResponse;
import store.sonyk9919.api.domain.island.entity.Region;
import store.sonyk9919.api.domain.island.service.IslandRankingService;

@RestController
@RequestMapping("/v1/rankings")
@RequiredArgsConstructor
public class IslandRankingController {

    private final IslandRankingService islandRankingService;

    @Operation(summary = "랭킹 조회", description = "region 파라미터가 없으면 전체 랭킹, 있으면 해당 지역 랭킹을 조회합니다. 상위 100위와 내 순위를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "섬을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public IslandRankingResponse getRanking(
            @RequestParam(required = false) Region region,
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember
    ) {
        return islandRankingService.getRanking(region, authMember.getId());
    }
}
