package kr.hhplus.be.server.order.domain;

import kr.hhplus.be.server.order.infra.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OrderOutboxRepository orderOutboxRepository;
    private final KafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 60000)
    public void republish() {
        List<OrderOutbox> pendingEvents = orderOutboxRepository.findByStatus(OutboxStatus.PENDING);

        for (OrderOutbox orderOutbox : pendingEvents) {
            try {
                kafkaProducer.publish("order-completed", orderOutbox.getPayload());
                orderOutbox.updateStatus(OutboxStatus.PUBLISHED);
            } catch (Exception e) {
                log.error("kafka 전송 실패 - orderId : {}", orderOutbox.getOrderId(), e);
                orderOutbox.incrementRetryCount();
                if(orderOutbox.getRetryCount() >= 3 ){
                    orderOutbox.updateStatus(OutboxStatus.FAILED);
                }
            } finally {
                orderOutboxRepository.save(orderOutbox);
            }
        }
    }
}
