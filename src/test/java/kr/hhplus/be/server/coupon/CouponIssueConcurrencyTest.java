package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponRepository;
import kr.hhplus.be.server.coupon.domain.CouponService;
import kr.hhplus.be.server.coupon.domain.IssueCouponCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class CouponIssueConcurrencyTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("최대 발급 수량이 5개인 쿠폰에 대해 동시에 10개의 쿠폰 발급을 요청하면 5개는 성공하고 5개는 실패한다")
    void testCouponIssue() throws InterruptedException {

        Coupon coupon = Coupon.builder()
                .issueLimit(5)
                .issuedCount(0)
                .build();

        couponRepository.save(coupon);

        int threadCount = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0); // 성공 카운터
        AtomicInteger failureCount = new AtomicInteger(0); // 실패 카운터

        for (long i = 0; i < threadCount; i++) {
            long userId = i + 1; // 각 스레드마다 고유 유저 ID 생성
            executorService.execute(() -> {
                try {
                    IssueCouponCommand command = new IssueCouponCommand(coupon.getId(), userId);
                    couponService.issueCoupon(command);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    System.err.println("예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown(); // 작업 완료
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Coupon afterCoupon = couponRepository.findById(coupon.getId()).orElseThrow(() -> new NotFoundException("쿠폰이 없습니다ㅏ"));

        assertThat(afterCoupon.getIssuedCount()).isEqualTo(5);

        assertThat(successCount.get()).isEqualTo(5);
        assertThat(failureCount.get()).isEqualTo(5);

    }

    @Test
    @DisplayName("REDIS : 최대 발급 수량이 10개인 쿠폰에 대해 동시에 100건의 쿠폰 발급을 요청하면 10개는 성공하고 90개는 실패한다")
    void testCouponIssueWithRedis() throws InterruptedException {

        Coupon coupon = Coupon.builder()
                .issueLimit(10)
                .issuedCount(0)
                .build();

        couponRepository.save(coupon);

        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0); // 성공 카운터
        AtomicInteger failureCount = new AtomicInteger(0); // 실패 카운터

        for (long i = 0; i < threadCount; i++) {
            long userId = i + 1; // 각 스레드마다 고유 유저 ID 생성
            executorService.execute(() -> {
                try {
                    IssueCouponCommand command = new IssueCouponCommand(coupon.getId(), userId);
                    couponService.issueCouponWithRedisLock(command);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    System.err.println("예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown(); // 작업 완료
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Coupon afterCoupon = couponRepository.findById(coupon.getId()).orElseThrow(() -> new NotFoundException("쿠폰이 없습니다"));

        assertThat(afterCoupon.getIssuedCount()).isEqualTo(10);

        assertThat(successCount.get()).isEqualTo(10);
        assertThat(failureCount.get()).isEqualTo(90);

    }


}
