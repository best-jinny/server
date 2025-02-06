package kr.hhplus.be.server.coupon.domain;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findByIdForUpdate(Long couponId);
    Optional<Coupon> findById(Long couponId);
    Coupon save(Coupon coupon);
    List<Long> findActiveCouponIds();
    List<Coupon> findActiveCoupons();
    void deleteAll();
}
