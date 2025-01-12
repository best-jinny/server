package kr.hhplus.be.server.coupon.interfaces.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.IssuedCouponResult;
import kr.hhplus.be.server.coupon.facade.CouponFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponFacade couponFacade;

    // 쿠폰 발급
    @PostMapping
    public ResponseEntity<?> issue(@Valid @RequestBody CouponIssueRequest request) {
        IssuedCouponResult coupon = couponFacade.issueCoupon(request.getCouponId(), request.getUserId());
        return ResponseEntity.ok(coupon);
    }

    // 쿠폰 조회
    @GetMapping
    public ResponseEntity<?> list( @RequestParam(value = "UserId", required = true) String UserId,
                                   @RequestParam(value = "isValid", required = false) Boolean isValid,
                                   @RequestParam(value = "isUsed", required = false) Boolean isUsed) {

        List<CouponResponse> couponResponse = new ArrayList<>();

        // 유효기간을 ISO 8601 형식으로 설정 (시간대 포함)
        String validDateTime = ZonedDateTime.now().plusDays(30).format(DateTimeFormatter.ISO_DATE_TIME);

        couponResponse.add(new CouponResponse("AAAA-AAAA-AAAA", "블랙프라이데이", validDateTime, true, false ));
        couponResponse.add(new CouponResponse("AAAA-AAAA-BBBB", "블랙프라이데이", validDateTime, true, false ));

        CouponListResponse response = new CouponListResponse(couponResponse);

        return ResponseEntity.ok(response);
    }
}
