package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.coupon.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
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
    private CouponIssuanceService couponIssuanceService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private IssuedCouponRepository issuedCouponRepository;

    @Autowired
    private CouponIssueProcessor couponIssueProcessor;

    @Autowired
    private CouponStockInitializer couponStockInitializer;

    @Autowired
    private RedissonClient redissonClient;

    @AfterEach
    void tearDown() {
        issuedCouponRepository.deleteAll();
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("V1(DB)-최대 발급 수량이 5개인 쿠폰에 대해 동시에 10개의 쿠폰 발급을 요청하면 5개는 성공하고 5개는 실패한다")
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
                    couponIssuanceService.issueCoupon(CouponStrategy.DB.name(), command);
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
        //assertThat(failureCount.get()).isEqualTo(5);

    }

    @Test
    @DisplayName("V2(REDIS)-최대 발급 수량이 5개인 쿠폰에 대해 동시에 10개의 쿠폰 발급을 요청하면 5개는 성공하고 5개는 실패한다")
    void testCouponIssueWithRedis() throws InterruptedException {

        Coupon coupon = Coupon.builder()
                .issueLimit(5)
                .issuedCount(0)
                .build();

        couponRepository.save(coupon);

        couponStockInitializer.initializeCouponStock();

        Set<Long> requestedUserIds = new ConcurrentSkipListSet<>();

        int threadCount = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);


        for (long i = 0; i < threadCount; i++) {
            long userId = i + 1; // 각 스레드마다 고유 유저 ID 생성
            executorService.execute(() -> {
                try {
                    IssueCouponCommand command = new IssueCouponCommand(coupon.getId(), userId);
                    couponIssuanceService.issueCoupon(CouponStrategy.REDIS.name(), command);
                    requestedUserIds.add(userId);
                } catch (Exception e) {
                    System.err.println("예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown(); // 작업 완료
                }
            });
        }

        latch.await();
        executorService.shutdown();

        couponIssueProcessor.processCouponQueue(coupon.getId());

        Coupon afterCoupon = couponRepository.findById(coupon.getId()).orElseThrow(() -> new NotFoundException("쿠폰이 없습니다"));

        String resultMapKey = "REQUEST-RESULT-" + coupon.getId();
        RMap<Long, CouponIssuanceStatus> resultMap = redissonClient.getMap(resultMapKey);

        long successCount = resultMap.values().stream().filter(status -> status.equals(CouponIssuanceStatus.COMPLETED)).count();
        long failureCount = resultMap.values().stream().filter(status -> status.equals(CouponIssuanceStatus.FAILED)).count();

        assertThat(afterCoupon.getIssuedCount()).isEqualTo(5);
        assertThat(successCount).isEqualTo(5);
        assertThat(failureCount).isEqualTo(5);
    }



}
