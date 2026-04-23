package store.sonyk9919.api.domain.island.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.island.dto.ResourceBalanceResponse;
import store.sonyk9919.api.domain.island.service.ResourceService;

@RestController
@RequestMapping("/v1/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @Operation(summary = "재화 잔액 조회", description = "인증된 회원의 현재 재화(조개, 보석, 연료) 잔액을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "섬을 찾을 수 없음")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceBalanceResponse getBalance(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember
    ) {
        return resourceService.getBalance(authMember.getId());
    }
}
