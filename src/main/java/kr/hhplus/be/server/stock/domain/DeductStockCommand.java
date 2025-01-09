package kr.hhplus.be.server.stock.domain;

import kr.hhplus.be.server.order.facade.OrderCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeductStockCommand {

    private Long productId;
    private int amount;

    public static DeductStockCommand of(OrderCriteria.OrderLineCriteria line) {
        return new DeductStockCommand(line.getProductId(), line.getQuantity());
    }
}
