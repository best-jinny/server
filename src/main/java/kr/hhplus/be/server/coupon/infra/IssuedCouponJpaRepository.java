package kr.hhplus.be.server.coupon.infra;

import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long> {
    boolean existsByCouponIdAndUserId(Long couponId, Long userId);
    Page<IssuedCoupon> findAllByUserId(Long userId, Pageable pageable);

    Long countIssuedCouponByCouponId(Long couponId);
}
