package store.sonyk9919.api.global.cache;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.sonyk9919.api.domain.building.service.BuildingMetadataCache;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.repository.LevelSpecRepository;
import store.sonyk9919.api.domain.shop.service.ShopItemCache;
import store.sonyk9919.api.domain.island.service.LevelSpecCache;
import store.sonyk9919.api.domain.shop.service.GemItemCache;

@ExtendWith(MockitoExtension.class)
class CacheWarmUpServiceTest {

    @Mock private LevelSpecRepository levelSpecRepository;
    @Mock private LevelSpecCache levelSpecCache;
    @Mock private BuildingMetadataCache buildingMetadataCache;
    @Mock private ShopItemCache shopItemCache;
    @Mock private GemItemCache gemItemCache;

    @InjectMocks private CacheWarmUpService cacheWarmUpService;

    @Test
    @DisplayName("서버 시작 시 정적 데이터 캐시를 미리 로딩한다")
    void warmUpStaticData() {

        // given
        LevelSpec level1 = mock(LevelSpec.class);
        LevelSpec level2 = mock(LevelSpec.class);

        given(level1.getLevel()).willReturn(1);
        given(level2.getLevel()).willReturn(2);

        given(levelSpecRepository.findAll()).willReturn(List.of(level1, level2));

        // when
        cacheWarmUpService.warmUpStaticData();

        // then
        then(buildingMetadataCache).should(times(1)).get();
        then(levelSpecRepository).should(times(1)).findAll();
        then(levelSpecCache).should(times(1)).get(1);
        then(levelSpecCache).should(times(1)).get(2);
        then(shopItemCache).should(times(1)).getAll();
        then(gemItemCache).should(times(1)).getAll();
    }
}