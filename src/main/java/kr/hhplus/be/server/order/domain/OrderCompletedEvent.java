package kr.hhplus.be.server.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCompletedEvent {
    private final Long orderId;
}
