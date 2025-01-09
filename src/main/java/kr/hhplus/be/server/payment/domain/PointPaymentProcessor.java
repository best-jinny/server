package kr.hhplus.be.server.payment.domain;

import kr.hhplus.be.server.point.domain.PointService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PointPaymentProcessor implements PaymentProcessor {

    private final PointService pointService;

    @Override
    public Payment process(PaymentCommand command) {
        pointService.deduct(command.getUserId(), command.getFinalPrice());
        return Payment.createPayment(command.getUserId(), command.getOrderId(), command.getPaymentMethod(), PaymentStatus.COMPLETED, command.getFinalPrice());
    }
}
