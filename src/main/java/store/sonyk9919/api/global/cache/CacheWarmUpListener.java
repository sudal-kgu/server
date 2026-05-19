package store.sonyk9919.api.global.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.LockStatus;

@Slf4j
@Profile("!test")
@Component
@RequiredArgsConstructor
public class CacheWarmUpListener {

    private final CacheWarmUpService cacheWarmUpService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            cacheWarmUpService.warmUpStaticData();
        } catch (CustomException e) {
            if (e.getStatus() == LockStatus.LOCK_ACQUISITION_FAILED) {
                log.info("다른 인스턴스에서 캐시 워밍업 수행 중입니다.");
                return;
            }
            throw e;
        }
    }
}
