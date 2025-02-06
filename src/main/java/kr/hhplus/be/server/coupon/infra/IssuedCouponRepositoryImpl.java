package kr.hhplus.be.server.coupon.infra;

import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public Optional<IssuedCoupon> findById(Long id) {
        return issuedCouponJpaRepository.findById(id);
    }

    @Override
    public boolean existsByCouponIdAndUserId(Long couponId, Long userId) {
        return issuedCouponJpaRepository.existsByCouponIdAndUserId(couponId, userId);
    }

    @Override
    public Page<IssuedCoupon> findAllByUserId(Long userId, Pageable pageable) {
        return issuedCouponJpaRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Long countByCouponId(Long couponId) {
        return issuedCouponJpaRepository.countIssuedCouponByCouponId(couponId);
    }

    @Override
    public void deleteAll() {
        issuedCouponJpaRepository.deleteAllInBatch();
    }

    @Override
    public IssuedCoupon save(IssuedCoupon coupon) {
       return issuedCouponJpaRepository.save(coupon);
    }

}
