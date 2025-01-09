package kr.hhplus.be.server.coupon.interfaces.controller;

import lombok.Getter;

@Getter
public class CouponIssueRequest {
    private Long couponId;
    private Long userId;
}
