package kr.hhplus.be.server.order.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCompletedEvent {
    private Long orderId;

    public OrderCompletedEvent(Long orderId) {
        this.orderId = orderId;
    }
}
