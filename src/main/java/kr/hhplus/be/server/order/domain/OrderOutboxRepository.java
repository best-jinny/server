package kr.hhplus.be.server.order.domain;

import java.util.List;

public interface OrderOutboxRepository {
    List<OrderOutbox> findByStatus(OutboxStatus status);
    void save(OrderOutbox orderOutbox);
    OrderOutbox findByOrderIdAndEventType(Long id, String eventType);
}
