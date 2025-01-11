package kr.hhplus.be.server.order;

import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderLine;
import kr.hhplus.be.server.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    @DisplayName("userId, orderLines 주어지면 totalPrice=총주문금액, discountPrice=0, finalPrice=totalPrice, Pending 상태의 Order 객체를 생성한다")
    void createOrder_ShouldCreateOrderSuccessfully() {
        // given
        Long userId = 1L;
        OrderLine orderLine1 = new OrderLine(1L, 1000L, 2);
        OrderLine orderLine2 = new OrderLine(2L, 2000L, 3);
        List<OrderLine> orderLines = List.of(orderLine1, orderLine2);
        // when
        Order order = Order.createOrder(userId, orderLines);
        // then
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getOrderLines()).isEqualTo(orderLines);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getTotalPrice()).isEqualTo(3000L);
        assertThat(order.getDiscountPrice()).isEqualTo(0L);
        assertThat(order.getFinalPrice()).isEqualTo(3000L);
    }

    @Test
    @DisplayName("할인 금액을 받아서 Order 에 할인을 적용하면 discountPrice, finalPrice 가 적용된 Order 객체를 반환한다")
    void applyDiscount_ShouldReturnOrderObjectSetDiscountPriceAndFinalPrice() {
        // given
        Long userId = 1L;
        OrderLine orderLine1 = new OrderLine(1L, 1000L, 2);
        OrderLine orderLine2 = new OrderLine(2L, 2000L, 3);
        List<OrderLine> orderLines = List.of(orderLine1, orderLine2);
        Order order = Order.createOrder(userId, orderLines);
        // when
        order.applyDiscount(500L);
        // then
        assertThat(order.getDiscountPrice()).isEqualTo(500L);
        assertThat(order.getFinalPrice()).isEqualTo(2500L);
    }

    @Test
    @DisplayName("할인 금액이 주문 금액보다 큰 경우 총 할인 금액은 주문 금액이다")
    void applyDiscount_whenDiscountAmountBiggerThanTotalAmountThenDiscountAmountEqualsTotalAmount() {
        // given
        Long userId = 1L;
        OrderLine orderLine1 = new OrderLine(1L, 1000L, 2);
        OrderLine orderLine2 = new OrderLine(2L, 2000L, 3);
        List<OrderLine> orderLines = List.of(orderLine1, orderLine2);
        Order order = Order.createOrder(userId, orderLines);
        // when
        order.applyDiscount(4000L);
        // then
        assertThat(order.getDiscountPrice()).isEqualTo(3000L);
        assertThat(order.getFinalPrice()).isEqualTo(0L);
    }

    @Test
    @DisplayName("주문을 완료하면 주문 상태가 PAID 로 바뀌어야 한다")
    void complete_ShouldChangeOrderStatusToPAID() {
        // given
        Long userId = 1L;
        OrderLine orderLine1 = new OrderLine(1L, 1000L, 2);
        OrderLine orderLine2 = new OrderLine(2L, 2000L, 3);
        List<OrderLine> orderLines = List.of(orderLine1, orderLine2);
        Order order = Order.createOrder(userId, orderLines);
        // when
        order.complete();
        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }






}
