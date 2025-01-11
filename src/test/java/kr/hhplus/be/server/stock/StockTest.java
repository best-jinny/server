package kr.hhplus.be.server.stock;


import kr.hhplus.be.server.common.exceptions.NotEnoughException;
import kr.hhplus.be.server.stock.domain.Stock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class StockTest {
    @Test
    @DisplayName("주문 수량보다 재고가 적으면 NotEnoughException 이 발생한다")
    void deduct_whenInsufficientStock_thenThrowNotEnoughException() {
        // given
        Stock stock = Stock.builder()
                .quantity(100)
                .build();
        // when & then
        assertThatThrownBy(() -> stock.deduct(101))
                .isInstanceOf(NotEnoughException.class);
    }

    @Test
    @DisplayName("재고가 0개일 때 차감하면 NotEnoughException 이 발생한다")
    void deduct_whenStockIsZero_throwNotEnoughException() {
        // given
        Stock stock = Stock.builder()
                .quantity(0)
                .build();
        // when & then
        assertThatThrownBy(() -> stock.deduct(1))
                .isInstanceOf(NotEnoughException.class);
    }

    @Test
    @DisplayName("재고가 100개 일 때 2개를 차감하면 98개가 남아야 한다")
    void deduct_whenSufficientStock_thenStockAmountDecreaseByGivenAmount() {
        // given
        Stock stock = Stock.builder()
                .quantity(100)
                .build();
        // when
        stock.deduct(2);
        // then
        assertThat(stock.getQuantity()).isEqualTo(98);
    }

    @Test
    @DisplayName("음수나 0인 수량이 주어지면 IllegalArgumentException 이 발생한다")
    void deduct_whenGivenNegativeAmount_thenThrowIllegalArgumentException() {
        // given
        Stock stock = Stock.builder()
                .quantity(100)
                .build();

        // when & then
        assertThatThrownBy(() -> stock.deduct(-1))
                .isInstanceOf(IllegalArgumentException.class);

        // when & then
        assertThatThrownBy(() -> stock.deduct(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}