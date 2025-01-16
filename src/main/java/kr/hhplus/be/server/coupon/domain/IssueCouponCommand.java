package kr.hhplus.be.server.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IssueCouponCommand {
    private Long couponId;
    private Long userId;
}
