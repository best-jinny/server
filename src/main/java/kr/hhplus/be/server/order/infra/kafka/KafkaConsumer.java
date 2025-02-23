package kr.hhplus.be.server.order.infra.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.OrderCompletedEvent;
import kr.hhplus.be.server.order.domain.OrderOutbox;
import kr.hhplus.be.server.order.domain.OrderOutboxRepository;
import kr.hhplus.be.server.order.domain.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final OrderOutboxRepository orderOutboxRepository;

    @KafkaListener(topics = "order-completed", groupId = "order-group")
    public void consume(String message) {
        log.info("order outbox - consume message: {}", message);
        try {
            OrderCompletedEvent event = objectMapper.readValue(message, OrderCompletedEvent.class);
            OrderOutbox outbox = orderOutboxRepository.findByOrderIdAndEventType(event.getOrderId(), event.getClass().getSimpleName());
            outbox.updateStatus(OutboxStatus.PUBLISHED);
            orderOutboxRepository.save(outbox);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
