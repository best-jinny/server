package kr.hhplus.be.server.coupon.domain;

import lombok.Getter;

@Getter
public class IssueCouponCommand {
    private Long couponId;
    private Long userId;
}
