package kr.hhplus.be.server.coupon.domain.strategy;

import kr.hhplus.be.server.coupon.domain.dto.IssueCouponCommand;
import kr.hhplus.be.server.coupon.domain.dto.CouponIssuanceResponse;

public interface CouponIssuanceStrategy {

    CouponIssuanceResponse issueCoupon(IssueCouponCommand command);

    String getType();


}
