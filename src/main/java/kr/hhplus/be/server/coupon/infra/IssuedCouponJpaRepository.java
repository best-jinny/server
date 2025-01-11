package kr.hhplus.be.server.coupon.infra;

import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long> {
    boolean existsByCouponIdAndUserId(Long couponId, Long userId);
}
