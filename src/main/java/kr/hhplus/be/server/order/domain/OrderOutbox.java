package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "`order_outbox`")
@Entity
public class OrderOutbox extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String eventType;
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @Builder
    public OrderOutbox(Long orderId, String eventType, String payload, OutboxStatus status) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
    }

    public void updateStatus(OutboxStatus status) {
        this.status = status;
    }

}
