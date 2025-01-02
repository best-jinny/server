package kr.hhplus.be.server.coupon.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CouponListResponse {
    private List<CouponResponse> coupons;
}
