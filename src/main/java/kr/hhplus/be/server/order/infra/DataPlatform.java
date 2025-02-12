package kr.hhplus.be.server.order.infra;

import kr.hhplus.be.server.order.domain.OrderCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class DataPlatform {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public boolean send(OrderCompletedEvent event) {
        log.info("주문 생성 완료 - 데이터 플랫폼으로 주문 정보 전달 - 주문 아이디 : {}", event.getOrderId());
        return true;
    }
}
