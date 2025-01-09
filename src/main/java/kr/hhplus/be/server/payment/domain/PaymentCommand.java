package kr.hhplus.be.server.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCommand {
    Long userId;
    Long orderId;
    Long finalPrice;
    PaymentMethod paymentMethod;

}
