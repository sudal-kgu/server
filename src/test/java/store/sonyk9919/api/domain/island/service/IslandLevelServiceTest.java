package store.sonyk9919.api.domain.island.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
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
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;
import store.sonyk9919.api.domain.building.service.BuildingMetadataCache;
import store.sonyk9919.api.domain.island.dto.ItemCatalogDto;
import store.sonyk9919.api.domain.island.dto.LevelUpResult;
import store.sonyk9919.api.domain.island.entity.Item;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;

@ExtendWith(MockitoExtension.class)
class IslandLevelServiceTest {

    @Mock private LevelSpecCache levelSpecCache;
    @Mock private ItemCache itemCache;
    @Mock private BuildingMetadataCache buildingMetadataCache;
    @Mock private MemberIslandRepository memberIslandRepository;
    @InjectMocks private IslandLevelService islandLevelService;

    private static final Long MEMBER_ID = 1L;

    private MemberIsland island;
    private LevelSpec currentSpec;
    private LevelSpec nextLevelSpec;

    @BeforeEach
    void setUp() {
        island = mock(MemberIsland.class);
        currentSpec = mock(LevelSpec.class);
        nextLevelSpec = mock(LevelSpec.class);
        given(memberIslandRepository.findByMemberAccountId(MEMBER_ID)).willReturn(Optional.of(island));
    }

    @Test
    void addRecyclingExp_이미_최대_레벨이면_notice가_null이다() {
        // given
        given(island.getLevel()).willReturn(7);
        given(levelSpecCache.get(7)).willReturn(currentSpec);
        given(island.isMaxLevel()).willReturn(true);
        stubIslandDtoFields();

        // when
        LevelUpResult result = islandLevelService.addRecyclingExp(MEMBER_ID);

        // then
        assertThat(result.getIsland()).isNotNull();
        assertThat(result.getNotice()).isNull();
    }

    @Test
    void addRecyclingExp_경험치_부족_시_notice가_null이다() {
        // given
        given(island.getLevel()).willReturn(1);
        given(levelSpecCache.get(1)).willReturn(currentSpec);
        given(island.isMaxLevel()).willReturn(false);
        given(island.canLevelUp(currentSpec)).willReturn(false);
        stubIslandDtoFields();

        // when
        LevelUpResult result = islandLevelService.addRecyclingExp(MEMBER_ID);

        // then
        assertThat(result.getIsland()).isNotNull();
        assertThat(result.getNotice()).isNull();
    }

    @Test
    void addItemExp_경험치_부족_시_notice가_null이다() {
        // given
        given(island.getLevel()).willReturn(2);
        given(island.isMaxLevel()).willReturn(false);
        given(levelSpecCache.get(2)).willReturn(currentSpec);
        given(island.canLevelUp(currentSpec)).willReturn(false);
        stubIslandDtoFields();

        // when
        LevelUpResult result = islandLevelService.addItemExp(MEMBER_ID, 100);

        // then
        assertThat(result.getIsland()).isNotNull();
        assertThat(result.getNotice()).isNull();
    }

    @Test
    void addRecyclingExp_레벨업_조건_충족_시_새_레벨과_해금_목록을_반환한다() throws Exception {
        // given
        given(island.getLevel()).willReturn(1, 1, 2, 2);
        given(levelSpecCache.get(1)).willReturn(currentSpec);
        given(levelSpecCache.get(2)).willReturn(nextLevelSpec);
        given(island.isMaxLevel()).willReturn(false, false);
        given(island.canLevelUp(currentSpec)).willReturn(true);
        stubIslandDtoFields();

        Item unlockedItem = createItem("해금아이템", 2);
        Item otherItem = createItem("다른아이템", 3);
        given(itemCache.getAll()).willReturn(List.of(unlockedItem, otherItem));

        BuildingCatalogDto unlockedBuilding = createBuilding("해금건물", 2);
        BuildingCatalogDto otherBuilding = createBuilding("다른건물", 3);
        given(buildingMetadataCache.get()).willReturn(List.of(unlockedBuilding, otherBuilding));

        // when
        LevelUpResult result = islandLevelService.addRecyclingExp(MEMBER_ID);

        // then
        assertThat(result.getIsland().getLevel()).isEqualTo(2);
        assertThat(result.getNotice().getUnlockedItems())
                .extracting(ItemCatalogDto::getName).containsExactly("해금아이템");
        assertThat(result.getNotice().getUnlockedBuildings())
                .extracting(BuildingCatalogDto::getName).containsExactly("해금건물");
    }

    @Test
    void addRecyclingExp_해당_레벨에_해금_콘텐츠_없으면_빈_목록을_반환한다() throws Exception {
        // given
        given(island.getLevel()).willReturn(1, 1, 2, 2);
        given(levelSpecCache.get(1)).willReturn(currentSpec);
        given(levelSpecCache.get(2)).willReturn(nextLevelSpec);
        given(island.isMaxLevel()).willReturn(false, false);
        given(island.canLevelUp(currentSpec)).willReturn(true);
        stubIslandDtoFields();
        given(itemCache.getAll()).willReturn(List.of(createItem("아이템", 3)));
        given(buildingMetadataCache.get()).willReturn(List.of(createBuilding("건물", 3)));

        // when
        LevelUpResult result = islandLevelService.addRecyclingExp(MEMBER_ID);

        // then
        assertThat(result.getIsland()).isNotNull();
        assertThat(result.getNotice().getUnlockedItems()).isEmpty();
        assertThat(result.getNotice().getUnlockedBuildings()).isEmpty();
    }

    @Test
    void addRecyclingExp_최종_레벨_도달_시_reachedMaxLevel이_true다() {
        // given
        given(island.getLevel()).willReturn(6, 6, 7);
        given(levelSpecCache.get(6)).willReturn(currentSpec);
        given(island.isMaxLevel()).willReturn(false, true);
        given(island.canLevelUp(currentSpec)).willReturn(true);
        stubIslandDtoFields();

        // when
        LevelUpResult result = islandLevelService.addRecyclingExp(MEMBER_ID);

        // then
        assertThat(result.getIsland().getLevel()).isEqualTo(7);
        assertThat(result.getNotice().isReachedMaxLevel()).isTrue();
    }

    @Test
    void addRecyclingExp_최종_레벨_미달_시_reachedMaxLevel이_false다() {
        // given
        given(island.getLevel()).willReturn(1, 1, 2, 2);
        given(levelSpecCache.get(1)).willReturn(currentSpec);
        given(levelSpecCache.get(2)).willReturn(nextLevelSpec);
        given(island.isMaxLevel()).willReturn(false, false);
        given(island.canLevelUp(currentSpec)).willReturn(true);
        stubIslandDtoFields();
        given(itemCache.getAll()).willReturn(List.of());
        given(buildingMetadataCache.get()).willReturn(List.of());

        // when
        LevelUpResult result = islandLevelService.addRecyclingExp(MEMBER_ID);

        // then
        assertThat(result.getNotice().isReachedMaxLevel()).isFalse();
    }

    @Test
    void addItemExp_레벨업_발생_시_새_레벨과_해금_목록을_반환한다() throws Exception {
        // given
        given(island.getLevel()).willReturn(2, 3, 3);
        given(island.isMaxLevel()).willReturn(false, false);
        given(levelSpecCache.get(2)).willReturn(currentSpec);
        given(levelSpecCache.get(3)).willReturn(nextLevelSpec);
        given(island.canLevelUp(currentSpec)).willReturn(true);
        stubIslandDtoFields();
        given(itemCache.getAll()).willReturn(List.of(createItem("레벨3아이템", 3)));
        given(buildingMetadataCache.get()).willReturn(List.of(createBuilding("레벨3건물", 3)));

        // when
        LevelUpResult result = islandLevelService.addItemExp(MEMBER_ID, 250);

        // then
        assertThat(result.getIsland().getLevel()).isEqualTo(3);
        assertThat(result.getNotice().getUnlockedItems())
                .extracting(ItemCatalogDto::getName).containsExactly("레벨3아이템");
        assertThat(result.getNotice().getUnlockedBuildings())
                .extracting(BuildingCatalogDto::getName).containsExactly("레벨3건물");
    }

    private void stubIslandDtoFields() {
        given(island.getNickname()).willReturn("테스트섬");
        given(island.getCumulativeExp()).willReturn(0);
        given(island.getRecyclingContributionExp()).willReturn(0);
        given(island.getItemContributionExp()).willReturn(0);
    }

    private Item createItem(String name, int unlockLevel) throws Exception {
        Constructor<Item> ctor = Item.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        Item item = ctor.newInstance();
        ReflectionTestUtils.setField(item, "name", name);
        ReflectionTestUtils.setField(item, "unlockLevel", unlockLevel);
        return item;
    }

    private BuildingCatalogDto createBuilding(String name, int requiredLevel) throws Exception {
        Constructor<BuildingCatalogDto> ctor = BuildingCatalogDto.class.getDeclaredConstructor(
                Long.class, String.class, String.class, String.class, int.class, int.class, int.class, int.class
        );
        ctor.setAccessible(true);
        return ctor.newInstance(null, name, "PRODUCTION", "model", requiredLevel, 0, 0, 0);
    }
}
