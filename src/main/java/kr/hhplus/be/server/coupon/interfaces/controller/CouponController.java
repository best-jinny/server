package kr.hhplus.be.server.coupon.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {

    // 쿠폰 발급
    @PostMapping
    public ResponseEntity<?> issue(@RequestBody CouponIssueRequest request) {
        if (request.getCustomerId() == null || request.getCouponId() == null ) {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        }

        CouponIssueResponse response = new CouponIssueResponse();
        return ResponseEntity.ok(response);
    }

    // 쿠폰 조회
    @GetMapping
    public ResponseEntity<?> list( @RequestParam(value = "customerId", required = true) String customerId,
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
