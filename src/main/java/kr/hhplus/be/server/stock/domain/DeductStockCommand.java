package kr.hhplus.be.server.stock.domain;

import kr.hhplus.be.server.order.facade.OrderCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class DeductStockCommand {

    private Long productId;
    private int amount;

    public static DeductStockCommand of(OrderCriteria.OrderLineCriteria line) {
        return new DeductStockCommand(line.getProductId(), line.getQuantity());
    }

    // OrderLineCriteria 리스트를 DeductStockCommand 리스트로 변환하는 책임을 추가
    public static List<DeductStockCommand> fromOrderLines(List<OrderCriteria.OrderLineCriteria> orderLines) {
        return orderLines.stream()
                .map(DeductStockCommand::of)
                .collect(Collectors.toList());
    }
}
