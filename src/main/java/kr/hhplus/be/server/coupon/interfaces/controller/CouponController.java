package kr.hhplus.be.server.coupon.interfaces.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.common.dto.PageResponse;
import kr.hhplus.be.server.coupon.domain.CouponIssuanceResponse;
import kr.hhplus.be.server.coupon.domain.CouponStrategy;
import kr.hhplus.be.server.coupon.domain.IssuedCouponResult;
import kr.hhplus.be.server.coupon.facade.CouponFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponFacade couponFacade;

    @PostMapping
    public ResponseEntity<?> issue(@Valid @RequestBody CouponIssueRequest request) {
        CouponIssuanceResponse coupon = couponFacade.issueCoupon(CouponStrategy.DB.name(), request.getCouponId(), request.getUserId());
        IssuedCouponResult response= coupon.getCouponResult();
        return ResponseEntity.ok(CouponIssueResponse.of(response));
    }

    // 사용 가능 쿠폰 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> availableCoupons(@PathVariable("userId") Long userId,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<IssuedCouponResult> issuedCoupons = couponFacade.getIssuedCoupons(userId, page, size);
        PageResponse<IssuedCouponResult> pageResponse = PageResponse.of(issuedCoupons);
        return ResponseEntity.ok(pageResponse);
    }
}
