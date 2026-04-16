package store.sonyk9919.api.global.common.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * Redis 락 키. SpEL 표현식 사용 가능 (예: "'island:' + #memberAccountId")
     */
    String key();

    /**
     * 락 획득을 기다리는 최대 시간 (기본값: 5초)
     */
    long waitTime() default 5;

    /**
     * 락을 자동으로 해제하는 시간 (기본값: 10초)
     */
    long leaseTime() default 10;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
