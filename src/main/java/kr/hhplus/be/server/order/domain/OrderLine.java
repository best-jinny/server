package kr.hhplus.be.server.order.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class OrderLine {

    private Long productId;
    private Long price;
    private int quantity;

    public static OrderLine of(long productId, long price, int quantity) {
        return new OrderLine(productId, price, quantity);
    }

}
