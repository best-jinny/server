package kr.hhplus.be.server.order.domain;

import kr.hhplus.be.server.order.facade.OrderCriteria;
import kr.hhplus.be.server.order.facade.OrderResult;
import kr.hhplus.be.server.order.interfaces.controller.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderResult createOrder(OrderCriteria criteria) {
        Order order = Order.createOrder(criteria.getUserId(), criteria.toOrderLines());
        //eventPublisher.publishEvent(new OrderCreatedEvent(order.getId()));
        return OrderResult.from(order);
    }

    public Order completeOrder(Order order) {
        order.complete();
        orderRepository.save(order);
        eventPublisher.publishEvent(new OrderCompletedEvent(order.getId()));
        return order;
    }

    public List<TopProductsResult> getTopProducts() {
        return orderRepository.findTopProducts(LocalDateTime.now().minusDays(2), 5);
    }
}
