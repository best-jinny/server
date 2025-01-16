package kr.hhplus.be.server.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order completeOrder(Order order) {
        order.complete();
        return orderRepository.save(order);
    }

    public List<TopProductsResult> getTopProducts() {
        return orderRepository.findTopProducts(LocalDateTime.now().minusDays(2), 5);
    }
}
