package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.coupon.domain.CouponService;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponFacade {
    private final UserService userService;
    private final CouponService couponService;

    @Transactional
    public IssuedCoupon issueCoupon(Long couponId, Long userId) {

        return couponService.issueCoupon(couponId, userId);

    }
}
