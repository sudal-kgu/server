package store.sonyk9919.api.domain.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import store.sonyk9919.api.domain.island.entity.IslandItemUsage;
import store.sonyk9919.api.domain.island.entity.Item;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.dto.LevelUpResult;
import store.sonyk9919.api.domain.island.repository.IslandItemUsageRepository;
import store.sonyk9919.api.domain.island.repository.ItemRepository;
import store.sonyk9919.api.domain.island.service.IslandLevelService;
import store.sonyk9919.api.domain.island.service.ItemCache;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.shop.dto.ShopItemResponse;
import store.sonyk9919.api.domain.shop.dto.ShopPurchaseResponse;
import store.sonyk9919.api.domain.shop.exception.ShopStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

    @Mock private ItemCache itemCache;
    @Mock private ItemRepository itemRepository;
    @Mock private IslandItemUsageRepository islandItemUsageRepository;
    @Mock private MemberIslandRegistryService memberIslandRegistryService;
    @Mock private ResourceService resourceService;
    @Mock private IslandLevelService islandLevelService;
    @InjectMocks private ShopService shopService;

    private static final Long MEMBER_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private List<Item> allItems;
    private Item targetItem;
    private MemberIsland island;

    @BeforeEach
    void setUp() throws Exception {
        targetItem = createItem(ITEM_ID, "쓰레기 제거 (소)", 100, 2, 3, 250);
        allItems = List.of(
                targetItem,
                createItem(2L, "쓰레기 제거 (대)", 100, 4, 3, 250),
                createItem(3L, "토양 정화",       50,  8, 3, 125),
                createItem(4L, "수질 개선",       50, 10, 3, 125),
                createItem(5L, "나무 심기",       30, 20, 3, 75)
        );
        island = mock(MemberIsland.class);
        given(memberIslandRegistryService.getIsland(MEMBER_ID)).willReturn(island);
    }

    private Item createItem(Long id, String name, int price, int maxCount, int unlockLevel, int expReward)
            throws Exception {
        Constructor<Item> ctor = Item.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        Item item = ctor.newInstance();
        ReflectionTestUtils.setField(item, "id", id);
        ReflectionTestUtils.setField(item, "name", name);
        ReflectionTestUtils.setField(item, "price", price);
        ReflectionTestUtils.setField(item, "maxCount", maxCount);
        ReflectionTestUtils.setField(item, "unlockLevel", unlockLevel);
        ReflectionTestUtils.setField(item, "expReward", expReward);
        return item;
    }

    @Test
    void getItems_아이템_5개를_반환한다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of());

        // when
        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        // then
        assertThat(result).hasSize(5);
    }

    @Test
    void getItems_IslandItemUsage_미존재_시_currentCount가_0이다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of());

        // when
        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        // then
        assertThat(result).allMatch(r -> r.getCurrentCount() == 0);
    }

    @Test
    void getItems_IslandItemUsage_존재_시_currentCount가_반영된다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemCache.getAll()).willReturn(allItems);
        IslandItemUsage usage = mockUsage(targetItem, 1L);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of(usage));

        // when
        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        // then
        ShopItemResponse response = result.stream()
                .filter(r -> r.getItemId().equals(targetItem.getId()))
                .findFirst().orElseThrow();
        assertThat(response.getCurrentCount()).isEqualTo(1L);
    }

    private IslandItemUsage mockUsage(Item item, long useCount) {
        IslandItemUsage usage = mock(IslandItemUsage.class);
        given(usage.getItem()).willReturn(item);
        given(usage.getUseCount()).willReturn(useCount);
        return usage;
    }

    @Test
    void getItems_레벨_조건_미달_시_purchasable이_false다() {
        // given
        given(island.getLevel()).willReturn(2);
        given(itemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of());

        // when
        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        // then
        assertThat(result).allMatch(r -> !r.isPurchasable());
    }

    @Test
    void getItems_레벨_충족_시_purchasable이_true다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of());

        // when
        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        // then
        assertThat(result).allMatch(ShopItemResponse::isPurchasable);
    }

    @Test
    void getItems_maxCount_도달_시_purchasable이_false다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemCache.getAll()).willReturn(allItems);
        IslandItemUsage usage = mockUsage(targetItem, 2L);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of(usage));

        // when
        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        // then
        ShopItemResponse response = result.stream()
                .filter(r -> r.getItemId().equals(targetItem.getId()))
                .findFirst().orElseThrow();
        assertThat(response.isPurchasable()).isFalse();
    }

    @Test
    void getItems_maxCount_미달_시_purchasable이_true다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemCache.getAll()).willReturn(allItems);
        IslandItemUsage usage = mockUsage(targetItem, 1L);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of(usage));

        // when
        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        // then
        ShopItemResponse response = result.stream()
                .filter(r -> r.getItemId().equals(targetItem.getId()))
                .findFirst().orElseThrow();
        assertThat(response.isPurchasable()).isTrue();
    }

    @Test
    void purchaseItem_정상_구매_시_조개가_차감되고_useCount가_증가한다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.of(targetItem));
        IslandItemUsage usage = mock(IslandItemUsage.class);
        given(usage.getUseCount()).willReturn(0L);
        given(islandItemUsageRepository.findByIslandAndItem(island, targetItem)).willReturn(Optional.of(usage));
        MemberResource updatedShell = mockShell(900L);
        given(resourceService.subtract(island, ResourceType.SHELL, 100)).willReturn(updatedShell);
        given(islandLevelService.addItemExp(MEMBER_ID, 250)).willReturn(LevelUpResult.none());

        // when
        ShopPurchaseResponse result = shopService.purchaseItem(MEMBER_ID, ITEM_ID);

        // then
        assertThat(result.getRemainingShell()).isEqualTo(900L);
        assertThat(result.getExpReward()).isEqualTo(250);
        then(usage).should().incrementUseCount();
    }

    @Test
    void purchaseItem_레벨업_발생_시_응답에_levelUpResult가_포함된다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.of(targetItem));
        IslandItemUsage usage = mock(IslandItemUsage.class);
        given(usage.getUseCount()).willReturn(0L);
        given(islandItemUsageRepository.findByIslandAndItem(island, targetItem)).willReturn(Optional.of(usage));
        MemberResource updatedShell = mockShell(900L);
        given(resourceService.subtract(island, ResourceType.SHELL, 100)).willReturn(updatedShell);
        LevelUpResult levelUpResult = LevelUpResult.of(4, false, List.of(), List.of("재활용 분류기"));
        given(islandLevelService.addItemExp(MEMBER_ID, 250)).willReturn(levelUpResult);

        // when
        ShopPurchaseResponse result = shopService.purchaseItem(MEMBER_ID, ITEM_ID);

        // then
        assertThat(result.getLevelUpResult().isLevelUp()).isTrue();
        assertThat(result.getLevelUpResult().getNewLevel()).isEqualTo(4);
        assertThat(result.getLevelUpResult().getUnlockedBuildings()).containsExactly("재활용 분류기");
    }

    private MemberResource mockShell(long amount) {
        MemberResource shell = mock(MemberResource.class);
        given(shell.getAmount()).willReturn(amount);
        return shell;
    }

    @Test
    void purchaseItem_존재하지_않는_아이템_구매_시_예외가_발생한다() {
        // given
        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> shopService.purchaseItem(MEMBER_ID, ITEM_ID))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ShopStatus.ITEM_NOT_FOUND.getMessage());
    }

    @Test
    void purchaseItem_레벨_미달_시_예외가_발생한다() {
        // given
        given(island.getLevel()).willReturn(2);
        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.of(targetItem));

        // when & then
        assertThatThrownBy(() -> shopService.purchaseItem(MEMBER_ID, ITEM_ID))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ShopStatus.ITEM_LOCKED.getMessage());
    }

    @Test
    void purchaseItem_maxCount_초과_시_예외가_발생한다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.of(targetItem));
        IslandItemUsage usage = mock(IslandItemUsage.class);
        given(usage.getUseCount()).willReturn(2L);
        given(islandItemUsageRepository.findByIslandAndItem(island, targetItem)).willReturn(Optional.of(usage));

        // when & then
        assertThatThrownBy(() -> shopService.purchaseItem(MEMBER_ID, ITEM_ID))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ShopStatus.ITEM_PURCHASE_LIMIT_EXCEEDED.getMessage());
    }

    @Test
    void purchaseItem_IslandItemUsage_미존재_시_새로_생성하여_구매한다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.of(targetItem));
        given(islandItemUsageRepository.findByIslandAndItem(island, targetItem)).willReturn(Optional.empty());
        given(islandItemUsageRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        MemberResource updatedShell = mockShell(900L);
        given(resourceService.subtract(island, ResourceType.SHELL, 100)).willReturn(updatedShell);
        given(islandLevelService.addItemExp(any(), anyInt())).willReturn(LevelUpResult.none());

        // when
        ShopPurchaseResponse result = shopService.purchaseItem(MEMBER_ID, ITEM_ID);

        // then
        assertThat(result.getCurrentCount()).isEqualTo(1L);
    }

}
