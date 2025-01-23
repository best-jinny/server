package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponRepository;
import kr.hhplus.be.server.coupon.domain.CouponService;
import kr.hhplus.be.server.coupon.domain.IssueCouponCommand;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Testcontainers
public class RedissonLockAspectIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponService couponService;

    @Test
    public void testLockAcquiredAndReleased() {

        User user = User.builder().build();

        userRepository.save(user);

        Coupon coupon = Coupon.builder()
                .issueLimit(5)
                .issuedCount(0)
                .build();

        couponRepository.save(coupon);

        IssueCouponCommand command = new IssueCouponCommand(coupon.getId(), user.getId());

        assertDoesNotThrow(() -> couponService.issueCouponWithRedisLock(command));


    }

}