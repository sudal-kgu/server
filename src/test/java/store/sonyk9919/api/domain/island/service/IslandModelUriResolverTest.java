package store.sonyk9919.api.domain.island.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.repository.IslandItemUsageRepository;
import store.sonyk9919.api.domain.shop.entity.ShopItem;
import store.sonyk9919.api.domain.shop.service.ShopItemCache;

@ExtendWith(MockitoExtension.class)
class IslandModelUriResolverTest {

    @Mock private IslandItemUsageRepository islandItemUsageRepository;
    @Mock private ShopItemCache shopItemCache;
    @InjectMocks private IslandModelUriResolver islandModelUriResolver;

    private static final String MODEL_BASE_URL = "https://image.sonyk9919.store:20024";
    private MemberIsland island;
    private ShopItem soilPurificationItem;

    @BeforeEach
    void setUp() {
        island = mock(MemberIsland.class);
        soilPurificationItem = mock(ShopItem.class);
        ReflectionTestUtils.setField(islandModelUriResolver, "modelBaseUrl", MODEL_BASE_URL);
        given(shopItemCache.getAll()).willReturn(List.of(soilPurificationItem));
        given(soilPurificationItem.getCode()).willReturn("SOIL_PURIFICATION");
    }

    @Test
    void resolve_레벨1_토양정화_미구매_시_island_1_모델을_반환한다() {
        // given
        given(island.getLevel()).willReturn(1);
        given(islandItemUsageRepository.findByIslandAndItem(island, soilPurificationItem))
                .willReturn(Optional.empty());

        // when
        String modelUri = islandModelUriResolver.resolve(island);

        // then
        assertThat(modelUri).isEqualTo(MODEL_BASE_URL + "/island_1.glb");
    }

    @Test
    void resolve_레벨2_토양정화_미구매_시_island_2_모델을_반환한다() {
        // given
        given(island.getLevel()).willReturn(2);
        given(islandItemUsageRepository.findByIslandAndItem(island, soilPurificationItem))
                .willReturn(Optional.empty());

        // when
        String modelUri = islandModelUriResolver.resolve(island);

        // then
        assertThat(modelUri).isEqualTo(MODEL_BASE_URL + "/island_2.glb");
    }

    @Test
    void resolve_레벨3이상_토양정화_미구매_시_island_2_모델을_반환한다() {
        // given
        given(island.getLevel()).willReturn(3);
        given(islandItemUsageRepository.findByIslandAndItem(island, soilPurificationItem))
                .willReturn(Optional.empty());

        // when
        String modelUri = islandModelUriResolver.resolve(island);

        // then
        assertThat(modelUri).isEqualTo(MODEL_BASE_URL + "/island_2.glb");
    }

    @Test
    void resolve_레벨3이상_토양정화_1회_구매_시_island_3_모델을_반환한다() {
        // given
        IslandItemUsage usage = mock(IslandItemUsage.class);
        given(island.getLevel()).willReturn(3);
        given(islandItemUsageRepository.findByIslandAndItem(island, soilPurificationItem))
                .willReturn(Optional.of(usage));
        given(usage.getUseCount()).willReturn(1L);

        // when
        String modelUri = islandModelUriResolver.resolve(island);

        // then
        assertThat(modelUri).isEqualTo(MODEL_BASE_URL + "/island_3.glb");
    }

    @Test
    void resolve_레벨3이상_토양정화_4회_구매_시_island_6_모델을_반환한다() {
        // given
        IslandItemUsage usage = mock(IslandItemUsage.class);
        given(island.getLevel()).willReturn(5);
        given(islandItemUsageRepository.findByIslandAndItem(island, soilPurificationItem))
                .willReturn(Optional.of(usage));
        given(usage.getUseCount()).willReturn(4L);

        // when
        String modelUri = islandModelUriResolver.resolve(island);

        // then
        assertThat(modelUri).isEqualTo(MODEL_BASE_URL + "/island_6.glb");
    }
}
