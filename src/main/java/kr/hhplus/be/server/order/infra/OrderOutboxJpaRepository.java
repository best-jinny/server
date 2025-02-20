package kr.hhplus.be.server.order.infra;

import kr.hhplus.be.server.order.domain.OrderOutbox;
import kr.hhplus.be.server.order.domain.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutbox, Long> {
    List<OrderOutbox> findByStatus(OutboxStatus status);
    OrderOutbox findByOrderIdAndEventType(Long id, String eventType);
}
