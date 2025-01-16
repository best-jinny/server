package kr.hhplus.be.server.coupon.interfaces.controller;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssueRequest {
    @NotNull(message = "유효하지 않은 쿠폰 아이디")
    private Long couponId;
    @NotNull(message = "유효하지 않은 유저 아이디")
    private Long userId;
}
