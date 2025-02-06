package kr.hhplus.be.server.coupon.domain;

import kr.hhplus.be.server.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueProcessor {

    private static final String COUPON_QUEUE_PREFIX = "COUPON-REQUEST-";
    private static final String ISSUED_COUPONS_PREFIX = "COUPON-ISSUED-";
    private static final String REQUEST_RESULT_PREFIX = "REQUEST-RESULT-";
    private static final String COUPON_STOCK_PREFIX = "COUPON-STOCK-";

    private final RedissonClient redissonClient;
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    @Async("couponTaskExecutor")
    public void asyncProcessCouponQueue(Long couponId) {
        try {
            processCouponQueue(couponId);
        } catch (Exception e) {
            log.error("쿠폰 처리 중 오류 발생: couponId={}, error={}", couponId, e.getMessage(), e);
            // todo 오류 처리
        }
    }

    public void processCouponQueue(Long couponId) {
        String queueKey = COUPON_QUEUE_PREFIX + couponId;
        String issuedKey = ISSUED_COUPONS_PREFIX + couponId;
        String resultMapKey = REQUEST_RESULT_PREFIX + couponId;
        String remainingKey = COUPON_STOCK_PREFIX + couponId;

        RScoredSortedSet<Long> queue = redissonClient.getScoredSortedSet(queueKey);
        RSet<Long> issuedSet = redissonClient.getSet(issuedKey);
        RMap<Long, CouponIssuanceStatus> resultMap = redissonClient.getMap(resultMapKey);
        RAtomicLong remainingCount = redissonClient.getAtomicLong(remainingKey);

        //log.info("현재 큐 상태: {}", queue.readAll());
        //log.info("현재 발급된 사용자 목록: {}", issuedSet.readAll());

        int maxProcess = 100;

        for (int i = 0; i < maxProcess; i++) {
            Long userId = queue.pollFirst();
            if (userId == null) {
                break;  // 더 이상 처리할 유저가 없으면 종료
            }

            // 이미 발급된 사용자라면 건너뛰고 계속 진행
            if (issuedSet.contains(userId)) {
                log.warn("이미 발급된 사용자입니다: userId={}", userId);
                resultMap.put(userId, CouponIssuanceStatus.COMPLETED);
                continue;
            }

            try {
                issueCoupon(couponId, userId); // 발급 처리 (DB)
                issuedSet.add(userId);
                resultMap.put(userId, CouponIssuanceStatus.COMPLETED);
                remainingCount.decrementAndGet();
                log.info("쿠폰 발급 성공: couponId={}, userId={}", couponId, userId);

            } catch (Exception e) {
                resultMap.put(userId, CouponIssuanceStatus.FAILED);
                log.error("쿠폰 발급 실패: couponId={}, userId={}", couponId, userId, e);
            }
        }
    }

    public void issueCoupon(Long couponId, Long userId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException("쿠폰을 찾을 수 없습니다."));
        IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, userId, LocalDateTime.now().plusDays(30));
        issuedCouponRepository.save(issuedCoupon);
        couponRepository.save(coupon);
    }


}
