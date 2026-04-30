package store.sonyk9919.api.global.common.lock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.global.common.exception.CustomException;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        List<String> lockKeys = resolveAllKeys(distributedLock, joinPoint);
        List<RLock> acquiredLocks = new ArrayList<>();

        try {
            for (String lockKey : lockKeys) {
                RLock lock = redissonClient.getLock(lockKey);
                boolean acquired = lock.tryLock(
                        distributedLock.waitTime(),
                        distributedLock.leaseTime(),
                        distributedLock.timeUnit()
                );
                if (!acquired) {
                    throw new CustomException(LockStatus.LOCK_ACQUISITION_FAILED);
                }
                acquiredLocks.add(lock);
            }
            return joinPoint.proceed();
        } finally {
            for (RLock lock : acquiredLocks) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    private List<String> resolveAllKeys(DistributedLock distributedLock, ProceedingJoinPoint joinPoint) {
        List<String> keys = new ArrayList<>();
        if (!distributedLock.key().isEmpty()) {
            keys.add("lock:" + resolveKey(distributedLock.key(), joinPoint));
        }
        for (String keyExpr : distributedLock.keys()) {
            keys.add("lock:" + resolveKey(keyExpr, joinPoint));
        }
        Collections.sort(keys);
        return keys;
    }

    private String resolveKey(String keyExpression, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return PARSER.parseExpression(keyExpression).getValue(context, String.class);
    }
}
