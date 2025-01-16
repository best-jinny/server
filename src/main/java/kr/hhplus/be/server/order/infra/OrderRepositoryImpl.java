package kr.hhplus.be.server.order.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderRepository;
import kr.hhplus.be.server.order.domain.TopProductsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.order.domain.QOrderLine.orderLine;
import static kr.hhplus.be.server.product.domain.QProduct.product;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public List<TopProductsResult> findTopProducts(LocalDateTime startDate, int limit) {
        return queryFactory
                .select(Projections.constructor(TopProductsResult.class,
                        orderLine.productId,
                        product.name,
                        orderLine.quantity.sum()))
                .from(orderLine)
                .join(product).on(orderLine.productId.eq(product.id))
                .where(orderLine.createdAt.goe(startDate))
                .groupBy(orderLine.productId, product.name)
                .orderBy(orderLine.quantity.sum().desc())
                .limit(limit)
                .fetch();
    }
}
