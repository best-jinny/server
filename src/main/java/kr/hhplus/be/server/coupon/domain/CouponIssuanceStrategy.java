package kr.hhplus.be.server.coupon.domain;

public interface CouponIssuanceStrategy {

    CouponIssuanceResponse issueCoupon(IssueCouponCommand command);

    String getType();


}
