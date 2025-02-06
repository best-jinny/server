package kr.hhplus.be.server.coupon.domain;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.List;


@Slf4j
@Repository
@RequiredArgsConstructor
public class CouponStockInitializer {

    private static final String COUPON_STOCK_PREFIX = "COUPON-STOCK-";
    private final RedissonClient redissonClient;
    private final CouponRepository couponRepository;

    @PostConstruct
    public void initializeCouponStock() {
        List<Coupon> allCoupons = couponRepository.findActiveCoupons(); // 발급 가능 쿠폰 재고 조회

        for (Coupon coupon : allCoupons) {
            String remainingKey = COUPON_STOCK_PREFIX + coupon.getId();
            long remainingCount = coupon.getIssueLimit() - coupon.getIssuedCount(); // 발급 가능 수량
            RAtomicLong atomicLong = redissonClient.getAtomicLong(remainingKey);
            atomicLong.set(remainingCount);
            log.info("remaining count: {}", remainingCount);
        }
    }
}
