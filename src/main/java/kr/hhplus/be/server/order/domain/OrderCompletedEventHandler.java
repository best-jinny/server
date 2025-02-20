package kr.hhplus.be.server.order.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.infra.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCompletedEventHandler {
    private final OrderOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(OrderCompletedEvent orderCompletedEvent) {
        log.info("order completed - save event: {}", orderCompletedEvent);

        try{
            String jsonPayload = objectMapper.writeValueAsString(orderCompletedEvent);
            OrderOutbox outbox = OrderOutbox.builder()
                    .orderId(orderCompletedEvent.getOrderId())
                    .eventType(orderCompletedEvent.getClass().getSimpleName())
                    .payload(jsonPayload)
                    .status(OutboxStatus.PENDING)
                    .build();

            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderInfo(OrderCompletedEvent orderCompletedEvent) throws JsonProcessingException {
        String jsonPayload = objectMapper.writeValueAsString(orderCompletedEvent);
        kafkaProducer.publish("order-completed", jsonPayload);
        log.info("kafka order-completed event published: {}", orderCompletedEvent);
    }

}
