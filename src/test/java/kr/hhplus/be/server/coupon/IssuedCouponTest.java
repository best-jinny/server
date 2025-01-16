package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.common.exceptions.ConflictException;
import kr.hhplus.be.server.common.exceptions.InvalidException;
import kr.hhplus.be.server.common.exceptions.NotEnoughException;
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
    @DisplayName("유효성 검사시 현재 시간이 발급된 쿠폰의 만료 시간 이후 일 경우 InvalidException 이 발생한다")
    void validate_expiry() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().minusDays(2);
        Coupon coupon = new Coupon();
        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .coupon(coupon)
                .expiredAt(expiredAt).build();
        // when & then
        assertThatThrownBy(issuedCoupon::validate)
                .isInstanceOf(InvalidException.class);
    }

    @Test
    @DisplayName("유효성 검사시 이미 USED 처리된 쿠폰은 ConflictException 이 발생한다")
    void validate_usage() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(2);
        Coupon coupon = new Coupon();
        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .coupon(coupon)
                .expiredAt(expiredAt)
                .status(CouponStatus.USED).build();
        // when & then
        assertThatThrownBy(issuedCoupon::validate)
                .isInstanceOf(ConflictException.class);
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

    @Test
    @DisplayName("쿠폰 발급시 발급 제한 수량을 초과하면 NotEnoughException 이 발생한다")
    void issue_whenIssuedCountExceedsIssueLimitThenThrowNotEnoughException() {
        // given
        Coupon coupon = Coupon.builder()
                .name("행운쿠폰")
                .issueLimit(100)
                .issuedCount(100)
                .build();
        // when & then
        assertThatThrownBy(() -> IssuedCoupon.issue(coupon, 1L, LocalDateTime.now().plusDays(7)))
                .isInstanceOf(NotEnoughException.class);
    }





}
