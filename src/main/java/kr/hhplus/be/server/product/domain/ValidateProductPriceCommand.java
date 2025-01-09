package kr.hhplus.be.server.product.domain;

import kr.hhplus.be.server.order.facade.OrderCriteria;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ValidateProductPriceCommand {
    private final Long productId;
    private final Long price;

    public ValidateProductPriceCommand(Long productId, Long price) {
        this.productId = productId;
        this.price = price;
    }

    public static List<ValidateProductPriceCommand> of(
            List<OrderCriteria.OrderLineCriteria> lines) {
        return lines.stream()
                .map(line -> new ValidateProductPriceCommand(line.getProductId(), line.getPrice()))
                .collect(Collectors.toList());
    }
}
