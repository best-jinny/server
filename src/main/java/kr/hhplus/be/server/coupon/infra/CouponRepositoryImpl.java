package kr.hhplus.be.server.coupon.infra;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Optional<Coupon> findByIdForUpdate(Long couponId) {
        return couponJpaRepository.findByIdForUpdate(couponId);
    }

    @Override
    public Optional<Coupon> findById(Long couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public List<Long> findActiveCouponIds() {
        return couponJpaRepository.findActiveCouponIds();
    }

    @Override
    public List<Coupon> findActiveCoupons() {
        return couponJpaRepository.findActiveCoupons();
    }

    @Override
    public void deleteAll() {
        couponJpaRepository.deleteAllInBatch();
    }
}
