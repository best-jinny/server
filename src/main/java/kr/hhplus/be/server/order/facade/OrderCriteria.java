package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.order.domain.OrderLine;
import kr.hhplus.be.server.order.interfaces.controller.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class OrderCriteria {

    private Long userId;
    private List<OrderLineCriteria> orderLines;
    private Long couponId;

    public static List<OrderLine> toOrderLines(List<OrderLineCriteria> orderLineCriterias) {
        return orderLineCriterias.stream()
                .map(criteria -> new OrderLine(criteria.getProductId(), criteria.getPrice(), criteria.getQuantity()))
                .toList();
    }

    public List<OrderLine> toOrderLines() {
        return toOrderLines(this.orderLines);
    }

    @Getter
    @AllArgsConstructor
    public static class OrderLineCriteria {
        private Long productId;
        private int quantity;
        private Long price;

        public OrderLineCriteria(OrderRequest.OrderLineRequest requestLine) {
            this.productId = requestLine.getProductId();
            this.quantity = requestLine.getQuantity();
            this.price = requestLine.getPrice();
        }
    }

    public static OrderCriteria of(OrderRequest request) {
        List<OrderLineCriteria> lines = request.getOrderLines()
                .stream()
                .map(OrderLineCriteria::new)
                .collect(Collectors.toList());

        return new OrderCriteria(
                request.getUserId(),
                lines,
                request.getCouponId()
        );
    }
}
