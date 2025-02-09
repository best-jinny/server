package kr.hhplus.be.server.coupon.domain;

import kr.hhplus.be.server.common.exceptions.InvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public Long calculateDiscountAmount(Long issuedCouponId, Long orderAmount) {

        if(issuedCouponId == null) {
            return 0L;
        }

        IssuedCoupon issuedCoupon = issuedCouponRepository.findByIdForUpdate(issuedCouponId)
                .orElseThrow(() -> new InvalidException("해당 쿠폰을 찾을 수 없습니다."));

        issuedCoupon.validate();
        Coupon coupon = issuedCoupon.getCoupon();
        return coupon.calculateDiscountAmount(orderAmount);
    }

    @Transactional
    public void redeemCoupon(Long issuedCouponId) {

        if(issuedCouponId == null) {
            return;
        }

        IssuedCoupon issuedCoupon = issuedCouponRepository.findById(issuedCouponId)
                .orElseThrow(() -> new InvalidException("해당 쿠폰을 찾을 수 없습니다."));
        issuedCoupon.markAsUsed();
        issuedCouponRepository.save(issuedCoupon);
    }


    public Page<IssuedCouponResult> getIssuedCouponsByUserId(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "issuedAt")); // 최신 발급순 정렬

        Page<IssuedCoupon> issuedCouponPage = issuedCouponRepository.findAllByUserId(userId, pageable);
        return issuedCouponPage.map(IssuedCouponResult::of);
    }

}
