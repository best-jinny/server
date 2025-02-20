package kr.hhplus.be.server.order.infra;

import kr.hhplus.be.server.order.domain.OrderOutbox;
import kr.hhplus.be.server.order.domain.OrderOutboxRepository;
import kr.hhplus.be.server.order.domain.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {

    private final OrderOutboxJpaRepository orderOutboxJpaRepository;

    @Override
    public List<OrderOutbox> findByStatus(OutboxStatus status) {
        return orderOutboxJpaRepository.findByStatus(status);
    }

    @Override
    public void save(OrderOutbox orderOutbox) {
        orderOutboxJpaRepository.save(orderOutbox);
    }

    @Override
    public OrderOutbox findByOrderIdAndEventType(Long id, String eventType) {
        return orderOutboxJpaRepository.findByOrderIdAndEventType(id, eventType);
    }

}
