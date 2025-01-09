package kr.hhplus.be.server.payment.domain;

import kr.hhplus.be.server.order.domain.Order;

public interface PaymentProcessor {
    Payment process(PaymentCommand command);
}
