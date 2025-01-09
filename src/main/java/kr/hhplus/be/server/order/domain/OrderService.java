package kr.hhplus.be.server.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order completeOrder(Order order) {
        order.complete();
        return orderRepository.save(order);
    }
}
