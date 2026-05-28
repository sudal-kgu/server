package store.sonyk9919.api.domain.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.shop.service.GemItemCache;
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

@ExtendWith(MockitoExtension.class)
class GemExchangeServiceTest {

    @Mock private GemItemCache gemItemCache;
    @Mock private GemItemRepository gemItemRepository;
    @Mock private MemberGemExchangeRepository memberGemExchangeRepository;
    @Mock private MemberIslandRegistryService memberIslandRegistryService;
    @Mock private MemberAccountService memberAccountService;
    @Mock private ResourceService resourceService;
    @InjectMocks private GemExchangeService gemExchangeService;

    private static final Long MEMBER_ID = 1L;
    private static final Long GEM_ITEM_ID = 1L;

    private GemItem gemItem;
    private MemberAccount memberAccount;
    private MemberIsland island;

    @BeforeEach
    void setUp() throws Exception {
        gemItem = createGemItem(GEM_ITEM_ID, "STARBUCKS_COUPON", "스타벅스 아메리카노 T", "스타벅스 아메리카노 Tall 사이즈 모바일 쿠폰", 40000, 3);
        memberAccount = mock(MemberAccount.class);
        island = mock(MemberIsland.class);
    }

    private void mockExchangeDependencies() {
        given(memberAccountService.getMemberAccount(MEMBER_ID)).willReturn(memberAccount);
        given(memberIslandRegistryService.getIsland(MEMBER_ID)).willReturn(island);
    }

    private GemItem createGemItem(Long id, String code, String name, String description, int gemCost, int monthlyLimit)
            throws Exception {
        Constructor<GemItem> ctor = GemItem.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        GemItem item = ctor.newInstance();
        ReflectionTestUtils.setField(item, "id", id);
        ReflectionTestUtils.setField(item, "code", code);
        ReflectionTestUtils.setField(item, "name", name);
        ReflectionTestUtils.setField(item, "description", description);
        ReflectionTestUtils.setField(item, "gemCost", gemCost);
        ReflectionTestUtils.setField(item, "monthlyLimit", monthlyLimit);
        return item;
    }

    private MemberResource mockGem(long amount) {
        MemberResource gem = mock(MemberResource.class);
        given(gem.getAmount()).willReturn(amount);
        return gem;
    }

    private MemberGemExchange mockExchange() {
        MemberGemExchange exchange = mock(MemberGemExchange.class);
        given(exchange.getCreatedAt()).willReturn(null);
        return exchange;
    }

    @Test
    void getGemItems_아이템_3개를_반환한다() throws Exception {
        // given
        List<GemItem> items = List.of(
                createGemItem(1L, "NATURALSTORE_DISCOUNT", "자연상점 할인 쿠폰",    "설명1", 20000, 5),
                createGemItem(2L, "STARBUCKS_COUPON",      "스타벅스 아메리카노 T", "설명2", 40000, 3),
                createGemItem(3L, "CONVENIENCE_COUPON",    "편의점 5,000원 금액권", "설명3", 50000, 5)
        );
        given(gemItemCache.getAll()).willReturn(items);

        // when
        List<GemItemResponse> result = gemExchangeService.getGemItems();

        // then
        assertThat(result).hasSize(3);
    }

    @Test
    void getGemItems_응답에_gemCost와_monthlyLimit이_포함된다() throws Exception {
        // given
        given(gemItemCache.getAll()).willReturn(List.of(gemItem));

        // when
        List<GemItemResponse> result = gemExchangeService.getGemItems();

        // then
        GemItemResponse response = result.get(0);
        assertThat(response.getGemCost()).isEqualTo(40000);
        assertThat(response.getMonthlyLimit()).isEqualTo(3);
    }

    @Test
    void exchange_정상_교환_시_보석이_차감되고_이력이_저장된다() {
        // given
        mockExchangeDependencies();
        given(gemItemRepository.findById(GEM_ITEM_ID)).willReturn(Optional.of(gemItem));
        given(memberGemExchangeRepository.countByMemberAccountAndGemItemAndCreatedAtBetween(
                eq(memberAccount), eq(gemItem), any(), any())).willReturn(0L);
        MemberResource updatedGem = mockGem(960000L);
        given(resourceService.subtract(island, ResourceType.GEM, 40000)).willReturn(updatedGem);
        MemberGemExchange exchange = mockExchange();
        given(memberGemExchangeRepository.save(any())).willReturn(exchange);

        // when
        GemExchangeResponse result = gemExchangeService.exchange(MEMBER_ID, GEM_ITEM_ID);

        // then
        assertThat(result.getRemainingGem()).isEqualTo(960000L);
        assertThat(result.getGemItemId()).isEqualTo(GEM_ITEM_ID);
        then(memberGemExchangeRepository).should().save(any(MemberGemExchange.class));
    }

    @Test
    void exchange_존재하지_않는_아이템_교환_시_예외가_발생한다() {
        // given
        mockExchangeDependencies();
        given(gemItemRepository.findById(GEM_ITEM_ID)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> gemExchangeService.exchange(MEMBER_ID, GEM_ITEM_ID))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ShopStatus.GEM_ITEM_NOT_FOUND.getMessage());
    }

    @Test
    void exchange_월_한도_초과_시_예외가_발생한다() {
        // given
        mockExchangeDependencies();
        given(gemItemRepository.findById(GEM_ITEM_ID)).willReturn(Optional.of(gemItem));
        given(memberGemExchangeRepository.countByMemberAccountAndGemItemAndCreatedAtBetween(
                eq(memberAccount), eq(gemItem), any(), any())).willReturn(3L);

        // when & then
        assertThatThrownBy(() -> gemExchangeService.exchange(MEMBER_ID, GEM_ITEM_ID))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ShopStatus.GEM_EXCHANGE_MONTHLY_LIMIT_EXCEEDED.getMessage());
    }

    @Test
    void exchange_보석_부족_시_예외가_발생한다() {
        // given
        mockExchangeDependencies();
        given(gemItemRepository.findById(GEM_ITEM_ID)).willReturn(Optional.of(gemItem));
        given(memberGemExchangeRepository.countByMemberAccountAndGemItemAndCreatedAtBetween(
                eq(memberAccount), eq(gemItem), any(), any())).willReturn(0L);
        given(resourceService.subtract(island, ResourceType.GEM, 40000))
                .willThrow(new CustomException(IslandStatus.INSUFFICIENT_AMOUNT));

        // when & then
        assertThatThrownBy(() -> gemExchangeService.exchange(MEMBER_ID, GEM_ITEM_ID))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(IslandStatus.INSUFFICIENT_AMOUNT.getMessage());
    }

    @Test
    void exchange_월_한도_미달_시_교환에_성공한다() {
        // given
        mockExchangeDependencies();
        given(gemItemRepository.findById(GEM_ITEM_ID)).willReturn(Optional.of(gemItem));
        given(memberGemExchangeRepository.countByMemberAccountAndGemItemAndCreatedAtBetween(
                eq(memberAccount), eq(gemItem), any(), any())).willReturn(2L);
        MemberResource updatedGem = mockGem(960000L);
        given(resourceService.subtract(island, ResourceType.GEM, 40000)).willReturn(updatedGem);
        MemberGemExchange exchange = mockExchange();
        given(memberGemExchangeRepository.save(any())).willReturn(exchange);

        // when
        GemExchangeResponse result = gemExchangeService.exchange(MEMBER_ID, GEM_ITEM_ID);

        // then
        assertThat(result).isNotNull();
    }
}
