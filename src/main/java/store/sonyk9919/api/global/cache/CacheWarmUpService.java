package store.sonyk9919.api.global.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.building.service.BuildingMetadataCache;
import store.sonyk9919.api.domain.island.repository.LevelSpecRepository;
import store.sonyk9919.api.domain.island.service.ItemCache;
import store.sonyk9919.api.domain.island.service.LevelSpecCache;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheWarmUpService {

    private final LevelSpecRepository levelSpecRepository;
    private final LevelSpecCache levelSpecCache;
    private final BuildingMetadataCache buildingMetadataCache;
    private final ItemCache itemCache;

    @DistributedLock(key = "'system:cache:warmup'", waitTime = 0, leaseTime = 30)
    public void warmUpStaticData() {
        log.info("[CacheWarmup] start");

        warmUpBuildingMetadata();
        warmUpLevelSpec();
        warmUpItems();

        log.info("[CacheWarmup] end");
    }

    private void warmUpBuildingMetadata() {
        buildingMetadataCache.get();
    }

    private void warmUpLevelSpec() {
        levelSpecRepository.findAll().forEach(
                spec -> levelSpecCache.get(spec.getLevel())
        );
    }

    private void warmUpItems() {
        itemCache.getAll();
    }
}
