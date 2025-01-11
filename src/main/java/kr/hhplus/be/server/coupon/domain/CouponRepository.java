package kr.hhplus.be.server.coupon.domain;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findByIdForUpdate(Long couponId);
}
