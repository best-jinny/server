package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResult {
    private Long id;
    private Long totalPrice;
    private Long discountPrice;
    private Long finalPrice;
    private OrderStatus status;

    public static OrderResult from(Order order) {
        return new OrderResult(
                order.getId(),
                order.getTotalPrice(),
                order.getDiscountPrice(),
                order.getFinalPrice(),
                order.getStatus()
        );
    }
}
