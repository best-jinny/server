package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.common.exceptions.NotEnoughException;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.DiscountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CouponTest {

    @Test
    @DisplayName("쿠폰의 할인 타입이 FIXED 면 discountValue 와 orderAmount 중 작은 금액을 반환한다")
    void calculateDiscountAmount_fixed() {
        // given
        Long orderAmount = 10_000L;
        Coupon coupon = Coupon.builder()
                .name("행운쿠폰")
                .discountType(DiscountType.FIXED)
                .discountValue(20000)
                .build();
        // when
        Long discountAmount = coupon.calculateDiscountAmount(orderAmount);
        // then
        assertThat(discountAmount).isEqualTo(10_000L);
    }

    @Test
    @DisplayName("쿠폰의 할인 타입이 PERCENTAGE 면 orderAmount 에서 discountValue/100 만큼 곱한 금액을 반환한다")
    void calculateDiscountAmount_percentage() {
        // given
        Long orderAmount = 10_000L;
        Coupon coupon = Coupon.builder()
                .name("행운쿠폰")
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(20)
                .build();
        // when
        Long discountAmount = coupon.calculateDiscountAmount(orderAmount);
        // then
        assertThat(discountAmount).isEqualTo(2000L);
    }

    @Test
    @DisplayName("발급된 쿠폰의 수가 발급 제한 수량보다 같거나 적은 경우 NotEnoughException 이 발생한다")
    void validateIssueLimit_whenIssuedCountLessThanIssuedLimitThenThrowNotEnoughException() {
        // given
        Coupon coupon = Coupon.builder()
                .name("행운쿠폰")
                .issueLimit(100)
                .issuedCount(100)
                .build();
        // when & then
        assertThatThrownBy(coupon::validateIssueLimit)
                .isInstanceOf(NotEnoughException.class);
    }

    @Test
    @DisplayName("쿠폰 발급 시 issuedCount 가 1씩 증가한다")
    void increaseIssuedCount_whenCouponIssuedThenIncreaseIssuedCountBy1() {
        // given
        Coupon coupon = Coupon.builder()
                .name("행운쿠폰")
                .issueLimit(100)
                .issuedCount(90)
                .build();
        // when
        coupon.increaseIssuedCount();
        // then
        assertThat(coupon.getIssuedCount()).isEqualTo(91);
    }
}
