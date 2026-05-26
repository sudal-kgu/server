package store.sonyk9919.api.domain.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.shop.dto.GemExchangeResponse;
import store.sonyk9919.api.domain.shop.dto.GemItemResponse;
import store.sonyk9919.api.domain.shop.dto.ShopItemResponse;
import store.sonyk9919.api.domain.shop.dto.ShopPurchaseResponse;
import store.sonyk9919.api.domain.shop.service.GemExchangeService;
import store.sonyk9919.api.domain.shop.service.ShopService;

@RestController
@RequestMapping("/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final GemExchangeService gemExchangeService;

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

    @Operation(summary = "아이템 구매", description = "조개를 소모해 아이템을 구매합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구매 성공"),
            @ApiResponse(responseCode = "400", description = "최대 구매 횟수 초과"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "해금 레벨 미달"),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없음")
    })
    @PostMapping("/items/{itemId}/purchases")
    @ResponseStatus(HttpStatus.OK)
    public ShopPurchaseResponse purchaseItem(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember,
            @PathVariable Long itemId
    ) {
        return shopService.purchaseItem(authMember.getId(), itemId);
    }

    @Operation(summary = "보석 교환 아이템 목록 조회", description = "보석으로 교환 가능한 현실 리워드 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/gemItems")
    @ResponseStatus(HttpStatus.OK)
    public List<GemItemResponse> getGemItems() {
        return gemExchangeService.getGemItems();
    }

    @Operation(summary = "보석으로 리워드 교환", description = "보석을 소모해 현실 리워드(기프티콘 등)로 교환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "교환 아이템을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "보석 부족 또는 락 획득 실패")
    })
    @PostMapping("/gemItems/{gemItemId}/exchange")
    @ResponseStatus(HttpStatus.OK)
    public GemExchangeResponse exchange(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember,
            @PathVariable Long gemItemId
    ) {
        return gemExchangeService.exchange(authMember.getId(), gemItemId);
    }
}
