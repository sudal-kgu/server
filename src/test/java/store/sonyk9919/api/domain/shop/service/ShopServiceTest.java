package store.sonyk9919.api.domain.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.List;
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
import store.sonyk9919.api.domain.island.repository.IslandItemUsageRepository;
import store.sonyk9919.api.domain.island.service.ItemCache;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.shop.dto.ShopItemResponse;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

    @Mock private ItemCache itemCache;
    @Mock private IslandItemUsageRepository islandItemUsageRepository;
    @Mock private MemberIslandRegistryService memberIslandRegistryService;
    @InjectMocks private ShopService shopService;

    private static final Long MEMBER_ID = 1L;
    private List<Item> allItems;
    private MemberIsland island;

    @BeforeEach
    void setUp() throws Exception {
        allItems = List.of(
                createItem(1L, "쓰레기 제거 (소)", 100, 2, 3, 250),
                createItem(2L, "쓰레기 제거 (대)", 100, 4, 3, 250),
                createItem(3L, "토양 정화",       50, 8, 3, 125),
                createItem(4L, "수질 개선",       50, 10, 3, 125),
                createItem(5L, "나무 심기",       30, 20, 3, 75)
        );
        island = mock(MemberIsland.class);
        given(memberIslandRegistryService.getIsland(MEMBER_ID)).willReturn(island);
        given(itemCache.getAll()).willReturn(allItems);
    }

    @Test
    void getItems_아이템_5개를_반환한다() {
        given(island.getLevel()).willReturn(3);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of());

        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        assertThat(result).hasSize(5);
    }

    @Test
    void getItems_IslandItemUsage_미존재_시_currentCount가_0이다() {
        given(island.getLevel()).willReturn(3);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of());

        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        assertThat(result).allMatch(r -> r.getCurrentCount() == 0);
    }

    @Test
    void getItems_IslandItemUsage_존재_시_currentCount가_반영된다() throws Exception {
        given(island.getLevel()).willReturn(3);

        Item targetItem = allItems.get(0);
        IslandItemUsage usage = mockUsage(targetItem, 1L);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of(usage));

        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        ShopItemResponse response = result.stream()
                .filter(r -> r.getItemId().equals(targetItem.getId()))
                .findFirst().orElseThrow();
        assertThat(response.getCurrentCount()).isEqualTo(1L);
    }

    @Test
    void getItems_레벨_조건_미달_시_purchasable이_false다() {
        given(island.getLevel()).willReturn(2);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of());

        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        assertThat(result).allMatch(r -> !r.isPurchasable());
    }

    @Test
    void getItems_레벨_충족_시_purchasable이_true다() {
        given(island.getLevel()).willReturn(3);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of());

        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        assertThat(result).allMatch(ShopItemResponse::isPurchasable);
    }

    @Test
    void getItems_maxCount_도달_시_purchasable이_false다() throws Exception {
        given(island.getLevel()).willReturn(3);

        Item targetItem = allItems.get(0);
        IslandItemUsage usage = mockUsage(targetItem, 2L);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of(usage));

        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        ShopItemResponse response = result.stream()
                .filter(r -> r.getItemId().equals(targetItem.getId()))
                .findFirst().orElseThrow();
        assertThat(response.isPurchasable()).isFalse();
    }

    @Test
    void getItems_maxCount_미달_시_purchasable이_true다() throws Exception {
        given(island.getLevel()).willReturn(3);

        Item targetItem = allItems.get(0);
        IslandItemUsage usage = mockUsage(targetItem, 1L);
        given(islandItemUsageRepository.findAllByIsland(island)).willReturn(List.of(usage));

        List<ShopItemResponse> result = shopService.getItems(MEMBER_ID);

        ShopItemResponse response = result.stream()
                .filter(r -> r.getItemId().equals(targetItem.getId()))
                .findFirst().orElseThrow();
        assertThat(response.isPurchasable()).isTrue();
    }

    private IslandItemUsage mockUsage(Item item, long useCount) {
        IslandItemUsage usage = mock(IslandItemUsage.class);
        given(usage.getItem()).willReturn(item);
        given(usage.getUseCount()).willReturn(useCount);
        return usage;
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
}
