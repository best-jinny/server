package kr.hhplus.be.server.order.interfaces.controller;

import kr.hhplus.be.server.order.domain.OrderStatus;
import kr.hhplus.be.server.order.facade.OrderResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private OrderStatus status;
    private Long totalPrice;
    private Long discountPrice;
    private Long finalPrice;

    public static OrderResponse of(OrderResult orderResult) {
        return new OrderResponse(
                orderResult.getId(),
                orderResult.getStatus(),
                orderResult.getTotalPrice(),
                orderResult.getDiscountPrice(),
                orderResult.getFinalPrice()
        );
    }
}
