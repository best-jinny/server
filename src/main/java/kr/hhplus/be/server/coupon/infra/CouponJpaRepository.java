package kr.hhplus.be.server.coupon.infra;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId")
    Optional<Coupon> findByIdForUpdate(@Param("couponId") Long couponId);

    @Query("SELECT c.id FROM Coupon c WHERE c.issuedCount < c.issueLimit")
    List<Long> findActiveCouponIds();

    @Query("SELECT c FROM Coupon c WHERE c.issuedCount < c.issueLimit")
    List<Coupon> findActiveCoupons();
}
