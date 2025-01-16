package kr.hhplus.be.server.point;

import kr.hhplus.be.server.common.exceptions.InvalidException;
import kr.hhplus.be.server.common.exceptions.NotEnoughException;
import kr.hhplus.be.server.point.domain.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PointTest {
    @Test
    @DisplayName("음수 포인트를 차감하려고 하면 InvalidException 이 발생한다")
    void deduct_whenGivenNegativeAmount_thenThrowInvalidException() {
        // given
        Point point = Point.builder()
                .userId(1L)
                .point(5000L)
                .build();

        // when & then
        assertThatThrownBy(() -> point.deduct(-2000L))
                .isInstanceOf(InvalidException.class);
    }

    @Test
    @DisplayName("포인트 잔고가 차감할 포인트보다 적으면 NotEnoughException 이 발생한다")
    void deduct_whenInsufficientPoint_thenThrowNotEnoughException() {
        // given
        Point point = Point.builder()
                .userId(1L)
                .point(5000L)
                .build();

        // when & then
        assertThatThrownBy(() -> point.deduct(10_000L))
                .isInstanceOf(NotEnoughException.class);
    }

    @Test
    @DisplayName("포인트 잔고가 0일 때 차감하면 NotEnoughException 이 발생한다")
    void deduct_whenPointIsZero_throwNotEnoughException() {
        // given
        Point point = Point.builder()
                .userId(1L)
                .point(0L)
                .build();

        // when & then
        assertThatThrownBy(() -> point.deduct(2000L))
                .isInstanceOf(NotEnoughException.class);
    }

    @Test
    @DisplayName("5000 포인트가 있을 때 2000을 차감하면 3000이 남아야 한다")
    void deduct_whenSufficientPoint_thenPointDecreaseByGivenAmount() {
        // given
        Point point = Point.builder()
                .userId(1L)
                .point(5000L)
                .build();

        // when
        point.deduct(2000L);

        // then
        assertThat(point.getPoint()).isEqualTo(3000L);
    }

    @Test
    @DisplayName("5000 포인트가 있을 때 3000을 충전하면 잔고가 8000 이 되어야한다")
    void charge_whenChargeAmount_thenPointIncreaseByGivenAmount() {
        // given
        Point point = Point.builder()
                .userId(1L)
                .point(5000L)
                .build();

        // when
        point.charge(3000L);

        // then
        assertThat(point.getPoint()).isEqualTo(8000L);
    }
}
