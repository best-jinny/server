package kr.hhplus.be.server.payment.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProcessorFactory paymentProcessorFactory;

    public void processPayment(PaymentCommand paymentCommand) {
        PaymentProcessor paymentProcessor = paymentProcessorFactory.getPaymentProcessor(paymentCommand.getPaymentMethod());
        Payment payment = paymentProcessor.process(paymentCommand);
        paymentRepository.save(payment);
    }
}
