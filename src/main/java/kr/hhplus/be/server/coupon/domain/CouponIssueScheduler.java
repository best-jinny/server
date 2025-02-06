package kr.hhplus.be.server.coupon.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponIssueScheduler {

    private final CouponRepository couponRepository;
    private final CouponIssueProcessor couponIssueProcessor;

    @Scheduled(fixedRate = 1000) // 0.5초마다 실행 : 500
    public void processQueueRequests() {
        List<Long> couponIds = couponRepository.findActiveCouponIds(); // 발급 가능한 쿠폰 ID 가져오기

        for (Long couponId : couponIds) {
            couponIssueProcessor.asyncProcessCouponQueue(couponId);
        }
    }

}
