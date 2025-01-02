package kr.hhplus.be.server.coupon.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponResponse {
    private String code;
    private String name;
    // todo 할인 금액 / 할인 유형 필요
    private String validDateTime;
    private boolean isValid;
    private boolean isUsed;
}
