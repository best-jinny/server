package kr.hhplus.be.server.payment.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long orderId;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private Long amount;
    private LocalDateTime paidAt;

    public static Payment createPayment(Long userId, Long orderId, PaymentMethod paymentMethod, PaymentStatus paymentStatus, Long amount) {
        Payment payment = new Payment();
        payment.userId = userId;
        payment.orderId = orderId;
        payment.paymentMethod = paymentMethod;
        payment.paymentStatus = paymentStatus;
        payment.amount = amount;
        payment.paidAt = LocalDateTime.now();
        return payment;
    }
}
