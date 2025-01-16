package kr.hhplus.be.server.coupon.interfaces.controller;

import kr.hhplus.be.server.coupon.domain.CouponStatus;
import kr.hhplus.be.server.coupon.domain.DiscountType;
import kr.hhplus.be.server.coupon.domain.IssuedCouponResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssueResponse {
    private Long id;
    private String couponName;
    private DiscountType discountType;
    private int discountValue;
    private CouponStatus status;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;

    public static CouponIssueResponse of(IssuedCouponResult coupon) {
        return new CouponIssueResponse(
                coupon.getId(),
                coupon.getCouponName(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                coupon.getStatus(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt()
        );
    }

}
