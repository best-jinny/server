package kr.hhplus.be.server.order.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataPlatform {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-completed", groupId = "data-group")
    public void consume(String message) {
       log.info("데이터 플랫폼 - consume order completed: {}", message);
        try {
            OrderCompletedEvent event = objectMapper.readValue(message, OrderCompletedEvent.class);
            send(event);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean send(OrderCompletedEvent event) {
        log.info("주문 생성 완료 - 데이터 플랫폼으로 주문 정보 전달 - 주문 아이디 : {}", event.getOrderId());
        return true;
    }
}
