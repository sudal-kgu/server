package store.sonyk9919.api.domain.island.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import store.sonyk9919.api.domain.island.dto.ItemCatalogDto;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.IslandItemUsageRepository;
import store.sonyk9919.api.domain.shop.entity.ShopItem;
import store.sonyk9919.api.domain.shop.service.ShopItemCache;
import store.sonyk9919.api.global.common.exception.CustomException;

@ExtendWith(MockitoExtension.class)
class ItemUsageServiceTest {

    @Mock private MemberIslandRegistryService memberIslandRegistryService;
    @Mock private IslandItemUsageRepository islandItemUsageRepository;
    @Mock private ShopItemCache shopItemCache;
    @InjectMocks private ItemUsageService itemUsageService;

    private static final Long MEMBER_ID = 1L;
    private List<ShopItem> allItems;
    private ShopItem targetItem;
    private MemberIsland island;

    @BeforeEach
    void setUp() throws Exception {
        targetItem = createItem(1L, "쓰레기 제거 (소)", 25, 10, 2, 50);
        allItems = List.of(
                targetItem,
                createItem(2L, "쓰레기 제거 (대)", 40,  3, 3, 70),
                createItem(3L, "토양 정화",        50,  4, 2, 125),
                createItem(4L, "수질 개선",        50, 10, 4, 125),
                createItem(5L, "나무 심기",        30, 10, 5, 75)
        );
        island = mock(MemberIsland.class);
        given(memberIslandRegistryService.getIsland(MEMBER_ID)).willReturn(island);
    }

    private ShopItem createItem(Long id, String name, int price, int maxCount, int unlockLevel, int expReward)
            throws Exception {
        Constructor<ShopItem> ctor = ShopItem.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        ShopItem item = ctor.newInstance();
        ReflectionTestUtils.setField(item, "id", id);
        ReflectionTestUtils.setField(item, "name", name);
        ReflectionTestUtils.setField(item, "price", price);
        ReflectionTestUtils.setField(item, "maxCount", maxCount);
        ReflectionTestUtils.setField(item, "unlockLevel", unlockLevel);
        ReflectionTestUtils.setField(item, "expReward", expReward);
        return item;
    }

    @Test
    void getItemUsages_아이템_5개를_반환한다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(shopItemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findItemIdAndUseCountByIsland(island)).willReturn(List.of());

        // when
        List<ItemCatalogDto> result = itemUsageService.getItemUsages(MEMBER_ID);

        // then
        assertThat(result).hasSize(5);
    }

    @Test
    void getItemUsages_사용_이력_없으면_currentCount가_0이다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(shopItemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findItemIdAndUseCountByIsland(island)).willReturn(List.of());

        // when
        List<ItemCatalogDto> result = itemUsageService.getItemUsages(MEMBER_ID);

        // then
        assertThat(result).allMatch(r -> r.getCurrentCount() == 0);
    }

    @Test
    void getItemUsages_사용_이력_있으면_currentCount가_반영된다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(shopItemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findItemIdAndUseCountByIsland(island))
                .willReturn(List.<Object[]>of(usageRow(targetItem.getId(), 1L)));

        // when
        List<ItemCatalogDto> result = itemUsageService.getItemUsages(MEMBER_ID);

        // then
        ItemCatalogDto response = result.stream()
                .filter(r -> r.getItemId().equals(targetItem.getId()))
                .findFirst().orElseThrow();
        assertThat(response.getCurrentCount()).isEqualTo(1L);
    }

    private Object[] usageRow(Long itemId, long useCount) {
        return new Object[]{itemId, useCount};
    }

    @Test
    void getItemUsages_레벨_미달_시_purchasable이_false다() {
        // given
        given(island.getLevel()).willReturn(1);
        given(shopItemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findItemIdAndUseCountByIsland(island)).willReturn(List.of());

        // when
        List<ItemCatalogDto> result = itemUsageService.getItemUsages(MEMBER_ID);

        // then
        assertThat(result).allMatch(r -> !r.isPurchasable());
    }

    @Test
    void getItemUsages_레벨_충족_시_purchasable이_true다() {
        // given
        given(island.getLevel()).willReturn(5);
        given(shopItemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findItemIdAndUseCountByIsland(island)).willReturn(List.of());

        // when
        List<ItemCatalogDto> result = itemUsageService.getItemUsages(MEMBER_ID);

        // then
        assertThat(result).allMatch(ItemCatalogDto::isPurchasable);
    }

    @Test
    void getItemUsages_maxCount_도달_시_purchasable이_false다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(shopItemCache.getAll()).willReturn(allItems);
        given(islandItemUsageRepository.findItemIdAndUseCountByIsland(island))
                .willReturn(List.<Object[]>of(usageRow(targetItem.getId(), 10L)));

        // when
        List<ItemCatalogDto> result = itemUsageService.getItemUsages(MEMBER_ID);

        // then
        ItemCatalogDto response = result.stream()
                .filter(r -> r.getItemId().equals(targetItem.getId()))
                .findFirst().orElseThrow();
        assertThat(response.isPurchasable()).isFalse();
    }

    @Test
    void getItemUsages_섬이_없으면_예외가_발생한다() {
        // given
        given(memberIslandRegistryService.getIsland(MEMBER_ID))
                .willThrow(new CustomException(IslandStatus.NOT_FOUND_ISLAND));

        // when & then
        assertThatThrownBy(() -> itemUsageService.getItemUsages(MEMBER_ID))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(IslandStatus.NOT_FOUND_ISLAND.getMessage());
    }
}
