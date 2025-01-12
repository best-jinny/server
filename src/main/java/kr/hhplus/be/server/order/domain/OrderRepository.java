package kr.hhplus.be.server.order.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository {
    Order save(Order order);
    List<TopProductsResult> findTopProducts(LocalDateTime startDate, int limit);
}
