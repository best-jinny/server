package kr.hhplus.be.server.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IssuedCouponResult {
    private Long id;
    private String couponName;
    private DiscountType discountType;
    private int discountValue;
    private CouponStatus status;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;

    public static IssuedCouponResult of(IssuedCoupon coupon) {
        return new IssuedCouponResult(
                coupon.getId(),
                coupon.getCoupon().getName(),
                coupon.getCoupon().getDiscountType(),
                coupon.getCoupon().getDiscountValue(),
                coupon.getStatus(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt()
        );
    }
}
