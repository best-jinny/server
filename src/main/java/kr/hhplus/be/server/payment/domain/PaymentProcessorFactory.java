package kr.hhplus.be.server.payment.domain;

import kr.hhplus.be.server.common.exceptions.InvalidException;
import kr.hhplus.be.server.point.domain.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessorFactory {
    private final PointService pointService;

    public PaymentProcessor getPaymentProcessor(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case POINT -> {
                return new PointPaymentProcessor(pointService);
            }
            default -> throw new InvalidException("지원하지 않는 결제 방법입니다.");
        }
    }
}
