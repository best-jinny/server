package kr.hhplus.be.server.coupon.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponIssueRequest {
    private String couponId;
    private Long customerId;
}
