package store.sonyk9919.api.domain.shop.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.member.service.MemberAccountService;
import store.sonyk9919.api.domain.shop.dto.GemExchangeResponse;
import store.sonyk9919.api.domain.shop.dto.GemItemResponse;
import store.sonyk9919.api.domain.shop.entity.GemItem;
import store.sonyk9919.api.domain.shop.entity.MemberGemExchange;
import store.sonyk9919.api.domain.shop.exception.ShopStatus;
import store.sonyk9919.api.domain.shop.repository.GemItemRepository;
import store.sonyk9919.api.domain.shop.repository.MemberGemExchangeRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GemExchangeService {

    private final GemItemCache gemItemCache;
    private final GemItemRepository gemItemRepository;
    private final MemberGemExchangeRepository memberGemExchangeRepository;
    private final MemberIslandRegistryService memberIslandRegistryService;
    private final MemberAccountService memberAccountService;
    private final ResourceService resourceService;

    public List<GemItemResponse> getGemItems() {
        return gemItemCache.getAll().stream()
                .map(GemItemResponse::from)
                .toList();
    }

    @DistributedLock(key = "'island:' + #memberId + ':gem'")
    @Transactional
    public GemExchangeResponse exchange(Long memberId, Long gemItemId) {
        MemberAccount memberAccount = memberAccountService.getMemberAccount(memberId);
        MemberIsland island = memberIslandRegistryService.getIsland(memberId);

        GemItem gemItem = gemItemRepository.findById(gemItemId)
                .orElseThrow(() -> new CustomException(ShopStatus.GEM_ITEM_NOT_FOUND));
        validateMonthlyLimit(memberAccount, gemItem);

        MemberResource updatedGem = resourceService.subtract(island, ResourceType.GEM, gemItem.getGemCost());
        MemberGemExchange exchange = memberGemExchangeRepository.save(
                MemberGemExchange.create(memberAccount, gemItem)
        );

        return GemExchangeResponse.of(exchange, gemItem, updatedGem.getAmount());
    }

    private void validateMonthlyLimit(MemberAccount memberAccount, GemItem gemItem) {
        YearMonth now = YearMonth.now();
        LocalDateTime start = now.atDay(1).atStartOfDay();
        LocalDateTime end = now.plusMonths(1).atDay(1).atStartOfDay();
        long monthlyCount = memberGemExchangeRepository
                .countByMemberAccountAndGemItemAndCreatedAtBetween(memberAccount, gemItem, start, end);
        if (monthlyCount >= gemItem.getMonthlyLimit()) {
            throw new CustomException(ShopStatus.GEM_EXCHANGE_MONTHLY_LIMIT_EXCEEDED);
        }
    }
}
