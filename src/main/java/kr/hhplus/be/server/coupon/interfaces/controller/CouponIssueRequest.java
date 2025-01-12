package kr.hhplus.be.server.coupon.interfaces.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CouponIssueRequest {
    @NotNull(message = "유효하지 않은 쿠폰 아이디")
    private Long couponId;
    @NotNull(message = "유효하지 않은 유저 아이디")
    private Long userId;
}
