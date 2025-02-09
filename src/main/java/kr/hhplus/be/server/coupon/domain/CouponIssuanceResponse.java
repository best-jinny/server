package kr.hhplus.be.server.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssuanceResponse {
    private CouponIssuanceStatus status;
    private String message;
    private IssuedCouponResult couponResult;
}
