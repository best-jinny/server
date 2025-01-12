package kr.hhplus.be.server.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopProductsResult {
    private Long productId;
    private String productName;
    private int totalQuantity;
}
