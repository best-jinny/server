package kr.hhplus.be.server.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResult {
    private Long id;
    private String name;
    private Long price;

    public static ProductResult of(Product product) {
        return new ProductResult(product.getId(), product.getName(), product.getPrice());
    }
}
