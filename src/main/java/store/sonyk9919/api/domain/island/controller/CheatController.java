package store.sonyk9919.api.domain.island.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.island.dto.ResourceBalanceResponse;
import store.sonyk9919.api.domain.island.service.CheatService;

@RestController
@RequestMapping("/v1/cheat")
@RequiredArgsConstructor
public class CheatController {

    private final CheatService cheatService;

    @Operation(summary = "치트 적용", description = "섬을 최종 레벨로 설정하고 모든 재화를 10,000,000으로 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "치트 적용 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "섬을 찾을 수 없음")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceBalanceResponse apply(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember
    ) {
        return cheatService.apply(authMember.getId());
    }
}
