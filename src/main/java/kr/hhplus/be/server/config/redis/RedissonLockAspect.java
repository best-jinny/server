package kr.hhplus.be.server.config.redis;

import kr.hhplus.be.server.coupon.domain.IssueCouponCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(kr.hhplus.be.server.config.redis.RedisLock) && args(issueCouponCommand)")
    public Object around(ProceedingJoinPoint joinPoint, IssueCouponCommand issueCouponCommand) throws Throwable {
        String lockKey = "LOCK_COUPON_" + issueCouponCommand.getCouponId();
        RLock lock = redissonClient.getLock(lockKey);

        boolean locked = lock.tryLock(1000, 500, TimeUnit.MILLISECONDS);
        if (!locked) {
            throw new RuntimeException("Lock 획득 실패");
        }

        log.info("Lock acquired for key: {}", lockKey);
        long startTime = System.nanoTime();  // 시작 시간 측정

        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.nanoTime();  // 종료 시간 측정
            log.info("Execution time for lock key {}: {} ms", lockKey, (endTime - startTime) / 1_000_000); // 밀리초로 변환
            lock.unlock();
            log.info("Lock released for key: {}", lockKey);
        }
    }
}
