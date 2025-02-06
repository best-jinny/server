package kr.hhplus.be.server.coupon.interfaces.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.coupon.domain.CouponIssuanceResponse;
import kr.hhplus.be.server.coupon.domain.CouponStrategy;
import kr.hhplus.be.server.coupon.facade.CouponFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v2/coupons")
@RequiredArgsConstructor
public class CouponControllerV2 {

    private final CouponFacade couponFacade;

    // 비동기 쿠폰 발급 (레디스)
    @PostMapping
    public ResponseEntity<?> issueAsync(@Valid @RequestBody CouponIssueRequest request) {
        CouponIssuanceResponse response = couponFacade.issueCoupon(CouponStrategy.REDIS.name(), request.getCouponId(), request.getUserId());
        return ResponseEntity.ok(response);
    }
}
