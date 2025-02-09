package kr.hhplus.be.server.coupon.domain;

import kr.hhplus.be.server.common.exceptions.InvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCouponIssuanceStrategy implements CouponIssuanceStrategy {

    private static final String COUPON_QUEUE_PREFIX = "COUPON-REQUEST-";
    private static final String ISSUED_COUPONS_PREFIX = "COUPON-ISSUED-";
    private static final String REQUEST_RESULT_PREFIX = "REQUEST-RESULT-";
    private static final String COUPON_STOCK_PREFIX = "COUPON-STOCK-";

    private final RedissonClient redissonClient;

    @Override
    public CouponIssuanceResponse issueCoupon(IssueCouponCommand command) {

        Long couponId = command.getCouponId();
        Long userId = command.getUserId();

        String queueKey = COUPON_QUEUE_PREFIX + couponId;
        String issuedKey = ISSUED_COUPONS_PREFIX + couponId;
        String remainingKey = COUPON_STOCK_PREFIX + couponId;
        String resultMapKey = REQUEST_RESULT_PREFIX + couponId;

        RScoredSortedSet<Long> queue = redissonClient.getScoredSortedSet(queueKey);
        RSet<Long> issuedSet = redissonClient.getSet(issuedKey);
        RMap<Long, CouponIssuanceStatus> resultMap = redissonClient.getMap(resultMapKey);
        RAtomicLong remainingCount = redissonClient.getAtomicLong(remainingKey);


        if (issuedSet.contains(userId)) {
            resultMap.put(userId, CouponIssuanceStatus.FAILED);
            throw new InvalidException("이미 발급 받은 쿠폰입니다.");
        }

        if (remainingCount.get() <= 0) {
            resultMap.put(userId, CouponIssuanceStatus.FAILED);
            throw new InvalidException("쿠폰이 마감되었습니다");
        }

        if (queue.contains(userId)) {
            resultMap.put(userId, CouponIssuanceStatus.FAILED);
            throw new InvalidException("이미 대기 중인 요청입니다.");
        }

        queue.addIfAbsent(System.currentTimeMillis(), userId);
        resultMap.put(userId, CouponIssuanceStatus.PENDING);

        queue.expire(Duration.ofSeconds(300));
        issuedSet.expire(Duration.ofSeconds(300));
        resultMap.expire(Duration.ofSeconds(300));

        log.info("쿠폰 발급 요청이 등록되었습니다: couponId={}, userId={}", couponId, userId);

        return new CouponIssuanceResponse(CouponIssuanceStatus.PENDING, "쿠폰 발급 요청이 접수되었습니다", null);
    }

    @Override
    public String getType() {
        return CouponStrategy.REDIS.name();
    }
}
