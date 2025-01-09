package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.common.exceptions.InvalidCouponException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final IssuedCouponRepository issuedCouponRepository;

    public Long calculateDiscountAmount(Long issuedCouponId, Long orderAmount) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.findByIdForUpdate(issuedCouponId)
                .orElseThrow(() -> new InvalidCouponException("해당 쿠폰을 찾을 수 없습니다."));

        issuedCoupon.validate();
        Coupon coupon = issuedCoupon.getCoupon();
        return coupon.calculateDiscountAmount(orderAmount);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void redeemCoupon(Long issuedCouponId) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.findByIdForUpdate(issuedCouponId)
                .orElseThrow(() -> new InvalidCouponException("해당 쿠폰을 찾을 수 없습니다."));
        issuedCoupon.markAsUsed();
        issuedCouponRepository.save(issuedCoupon);
    }

}
