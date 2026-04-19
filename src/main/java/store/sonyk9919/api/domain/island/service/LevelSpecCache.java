package store.sonyk9919.api.domain.island.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.LevelSpecRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class LevelSpecCache {

    private final LevelSpecRepository levelSpecRepository;

    @Cacheable(cacheNames = "level-spec", key = "#level")
    public LevelSpec get(int level) {
        return levelSpecRepository.findById(level)
                .orElseThrow(() -> new CustomException(IslandStatus.LEVEL_SPEC_NOT_FOUND));
    }
}
