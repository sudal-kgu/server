package store.sonyk9919.api.domain.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.shop.dto.ShopItemResponse;
import store.sonyk9919.api.domain.shop.service.ShopService;

@RestController
@RequestMapping("/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @Operation(summary = "상점 아이템 목록 조회", description = "상점에서 구매 가능한 아이템 목록과 사용자의 현재 구매 현황을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "섬을 찾을 수 없음")
    })
    @GetMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public List<ShopItemResponse> getItems(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember
    ) {
        return shopService.getItems(authMember.getId());
    }
}
