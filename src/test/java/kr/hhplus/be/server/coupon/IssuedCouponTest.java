package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.common.exceptions.InvalidCouponException;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponStatus;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class IssuedCouponTest {

    @Test
    @DisplayName("현재 시간이 발급된 쿠폰의 만료 시간 이후 일 경우 InvalidCouponException 이 발생한다")
    void validate_expiredCoupon() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().minusDays(2);
        Coupon coupon = new Coupon();
        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .coupon(coupon)
                .expiredAt(expiredAt).build();

        // when & then
        assertThatThrownBy(issuedCoupon::validate)
                .isInstanceOf(InvalidCouponException.class);
    }

    @Test
    @DisplayName("이미 USED 처리된 쿠폰인 경우 InvalidCouponException 이 발생한다")
    void validate_invalidCoupon() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().minusDays(2);
        Coupon coupon = new Coupon();
        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .coupon(coupon)
                .expiredAt(expiredAt)
                .status(CouponStatus.USED).build();

        // when & then
        assertThatThrownBy(issuedCoupon::validate)
                .isInstanceOf(InvalidCouponException.class);
    }

    @Test
    @DisplayName("유효한 쿠폰을 사용 처리하면 쿠폰 상태가 USED 로 바뀌어야한다")
    void markAsUsed_shouldChangeCouponStatusToUSED() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
        Coupon coupon = new Coupon();
        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .coupon(coupon)
                .expiredAt(expiredAt)
                .status(CouponStatus.VALID).build();

        // when
        issuedCoupon.markAsUsed();

        //then
        assertThat(issuedCoupon.getStatus()).isEqualTo(CouponStatus.USED);
    }



}
