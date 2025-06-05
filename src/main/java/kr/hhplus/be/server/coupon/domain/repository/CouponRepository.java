package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.Coupon;

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
