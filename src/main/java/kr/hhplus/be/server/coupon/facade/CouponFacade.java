package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.coupon.domain.*;
import kr.hhplus.be.server.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CouponFacade {
    private final UserService userService;
    private final CouponService couponService;
    private final CouponIssuanceService couponIssuanceService;

    @Transactional
    public CouponIssuanceResponse issueCoupon(String type, Long couponId, Long userId) {
        //userService.verify(userId);
        return couponIssuanceService.issueCoupon(type, new IssueCouponCommand(couponId, userId));
    }

    public Page<IssuedCouponResult> getIssuedCoupons(Long userId, int page, int size) {
        userService.verify(userId);
        return couponService.getIssuedCouponsByUserId(userId, page, size);
    }
}
