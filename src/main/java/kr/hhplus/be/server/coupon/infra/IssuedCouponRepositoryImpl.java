package kr.hhplus.be.server.coupon.infra;

import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {
    private final IssuedCouponJpaRepository issuedCouponJpaRepository;

    @Override
    public Optional<IssuedCoupon> findByIdForUpdate(Long id) {
        return issuedCouponJpaRepository.findById(id);
    }

    @Override
    public boolean existsByCouponIdAndUserId(Long couponId, Long userId) {
        return issuedCouponJpaRepository.existsByCouponIdAndUserId(couponId, userId);
    }

    @Override
    public IssuedCoupon save(IssuedCoupon coupon) {
       return issuedCouponJpaRepository.save(coupon);
    }

}
