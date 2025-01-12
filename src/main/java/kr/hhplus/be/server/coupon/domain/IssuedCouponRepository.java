package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IssuedCouponRepository {
    IssuedCoupon save(IssuedCoupon coupon);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ic FROM IssuedCoupon ic WHERE ic.id = :id")
    Optional<IssuedCoupon> findByIdForUpdate(Long id);

    boolean existsByCouponIdAndUserId(Long couponId, Long userId);

    Page<IssuedCoupon> findAllByUserId(Long userId, Pageable pageable);
}
